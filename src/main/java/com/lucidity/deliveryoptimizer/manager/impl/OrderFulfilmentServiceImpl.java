package com.lucidity.deliveryoptimizer.manager.impl;

import com.lucidity.deliveryoptimizer.calculator.cost.TravelingCostCalculator;
import com.lucidity.deliveryoptimizer.calculator.distance.DistanceCalculatorContext;
import com.lucidity.deliveryoptimizer.calculator.distance.HaversineDistanceCalculatorStrategy;
import com.lucidity.deliveryoptimizer.common.constants.Constant;
import com.lucidity.deliveryoptimizer.common.util.MinPrepareTimeGenerator;
import com.lucidity.deliveryoptimizer.domain.entry.*;
import com.lucidity.deliveryoptimizer.domain.enumuration.OrderStatus;
import com.lucidity.deliveryoptimizer.manager.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderFulfilmentServiceImpl implements OrderFulfilmentService {

    private final RestaurantManager restaurantManager;
    private final UserManager userManager;
    private final DeliveryAgentManager deliveryAgentManager;

    private final OrderManager orderManager;

    private final CostSolver costSolver;

    public OrderFulfilmentServiceImpl(RestaurantManager restaurantManager, UserManager userManager, DeliveryAgentManager deliveryAgentManager, OrderManager orderManager, CostSolver costSolver) {
        this.restaurantManager = restaurantManager;
        this.userManager = userManager;
        this.deliveryAgentManager = deliveryAgentManager;
        this.orderManager = orderManager;
        this.costSolver = costSolver;
    }


    public OrderEntry placeOrder(PlaceOrderInput input) {
        if (input == null || input.getUserId() == null || input.getRestaurantId() == null) {
            throw new RuntimeException("Mandatory fields missing in input while placing the order");
        }

        UserEntry userEntry = userManager.getUser(input.getUserId());
        if (userEntry == null
                || userEntry.getId() == null
                || StringUtils.isEmpty(userEntry.getName())
                || StringUtils.isEmpty(userEntry.getPhone())
                || userEntry.getAddress() == null
                || StringUtils.isEmpty(userEntry.getAddress().getAddress())
                || userEntry.getAddress().getLatitude() == null
                || userEntry.getAddress().getLongitude() == null) {
            throw new RuntimeException("User Mandatory Data missing while placing the order");
        }


        RestaurantEntry restaurantEntry = restaurantManager.getRestaurant(input.getRestaurantId());
        if (restaurantEntry == null
                || restaurantEntry.getId() == null
                || StringUtils.isEmpty(restaurantEntry.getName())
                || StringUtils.isEmpty(restaurantEntry.getPhone())
                || restaurantEntry.getAddress() == null
                || StringUtils.isEmpty(restaurantEntry.getAddress().getAddress())
                || restaurantEntry.getAddress().getLatitude() == null
                || restaurantEntry.getAddress().getLongitude() == null) {
            throw new RuntimeException("Restaurant Mandatory Data missing while placing the order");
        }

        //checking serviceability here
        if (!Boolean.TRUE.equals(restaurantEntry.getServiceable())) {
            throw new RuntimeException("Restaurant is Not Serviceable");
        }

        OrderEntry order = new OrderEntry();
        order.setRestaurantId(restaurantEntry.getId());
        order.setRestaurantName(restaurantEntry.getName());
        order.setRestaurantPhone(restaurantEntry.getPhone());
        order.setRestaurantAddress(restaurantEntry.getAddress().getAddress());
        order.setRestaurantLatitude(restaurantEntry.getAddress().getLatitude());
        order.setRestaurantLongitude(restaurantEntry.getAddress().getLongitude());
        //here we can configure this time on basis of  food item and restaurant
        //but here generating random numbers to simplify this
        if (input.getMinTimeToPrepareInMin() != null && input.getMinTimeToPrepareInMin() >= 0) {
            order.setMinTimeToPrepareInMinutes(input.getMinTimeToPrepareInMin());
        } else {
            order.setMinTimeToPrepareInMinutes(MinPrepareTimeGenerator.generateMinOrderPrepareTime());
        }


        order.setUserId(userEntry.getId());
        order.setUserName(userEntry.getName());
        order.setUserPhone(userEntry.getPhone());
        order.setUserAddress(userEntry.getAddress().getAddress());
        order.setUserLatitude(userEntry.getAddress().getLatitude());
        order.setUserLongitude(userEntry.getAddress().getLongitude());
        order = orderManager.createOrder(order);
        return order;
    }


    public OrderEntry assignOrder(AssignOrderInput input) {
        if (input == null || input.getOrderId() == null || input.getDeliveryAgentId() == null) {
            throw new RuntimeException("Mandatory Field Missing while Assigning order to agent");
        }

        OrderEntry orderEntry = orderManager.getOrder(input.getOrderId());

        if (orderEntry == null) {
            throw new RuntimeException("Order Not Found for Id=" + input.getOrderId());
        }
        if (orderEntry.getOrderStatus() == null || orderEntry.getOrderStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Order is not valid for assignment");
        }

        if (orderEntry.getDeliveryAgentId() != null) {
            throw new RuntimeException("Order is already assigned");
        }

        DeliveryAgentEntry deliveryAgentEntry = deliveryAgentManager.getAgent(input.getDeliveryAgentId());

        if (deliveryAgentEntry == null || deliveryAgentEntry.getId() == null) {
            throw new RuntimeException("Agent Not Found for Id=" + input.getDeliveryAgentId());
        }

        if (!Boolean.TRUE.equals(deliveryAgentEntry.getOnDuty())) {
            throw new RuntimeException("Agent is currently offline");
        }

        //checking if this agent has 2 active(Assigned+Picked) orders
        //if yes then do not assign order to this agent
        Long activeOrdersForAgent = orderManager.getActiveOrdersCountForAgent(deliveryAgentEntry.getId());
        if (activeOrdersForAgent != null && activeOrdersForAgent >= Constant.Common.ACTIVE_ORDER_THRESHHOLD_VALUE_FOR_AGENT) {
            throw new RuntimeException("Agent already have " + activeOrdersForAgent + " active orders please assign to other partner");
        }


        orderEntry.setOrderStatus(OrderStatus.ASSIGNED);
        if (orderEntry.getAssignedOn() == null) {
            if (input.getAssignedOn() != null) {
                orderEntry.setAssignedOn(input.getAssignedOn());
            } else {
                orderEntry.setAssignedOn(new Date());
            }
        }
        orderEntry.setDeliveryAgentId(deliveryAgentEntry.getId());

        orderEntry = orderManager.updateOrder(orderEntry.getId(), orderEntry);
        return orderEntry;
    }

    public List<OrderEntry> getActiveOrdersForDeliveryAgent(Long agentId) {
        return orderManager.getActiveOrdersForAgent(agentId);
    }

    public AllPossiblePathEntry calculateMinCostPath(OrdersDeliveryInput input) {

        if (input == null || input.getOrderIds() == null || input.getOrderIds().isEmpty() || input.getDeliveryAgentId() == null) {
            throw new RuntimeException("Empty Input for DeliveryTaskAssignment");
        }

        DistanceCalculatorContext distanceCalculatorContext = new DistanceCalculatorContext(new HaversineDistanceCalculatorStrategy());
        //here assuming that all orders assigned to deliveryAgent at the same time

        //assuming we have only max two orders
        List<OrderEntry> orders = new ArrayList<>();
        for (Long orderId : input.getOrderIds()) {
            OrderEntry orderEntry = orderManager.getOrder(orderId);
            if (orderEntry != null
                    && orderEntry.getDeliveryAgentId() != null
                    && orderEntry.getDeliveryAgentId().equals(input.getDeliveryAgentId())
                    && orderEntry.getOrderStatus() != null
                    && (OrderStatus.ASSIGNED == orderEntry.getOrderStatus() || OrderStatus.PICKED == orderEntry.getOrderStatus())) {
                orders.add(orderEntry);
            }
        }

        DeliveryAgentEntry agent = deliveryAgentManager.getAgent(input.getDeliveryAgentId());

        if (agent == null || agent.getId() == null || agent.getLatitude() == null || agent.getLongitude() == null) {
            throw new RuntimeException("Something Went Wrong With Agent Current Location");
        }

        //we will process only two orders at a time

        //do below steps for all combinations
        //here we are assuming that restaurant starts order preparation only after it assigned to agent(it should be on basis of order created on but to keep calculations simple we are using assignment time)
        //possible paths to deliver order
        //cost1=cost from agent to R1 min(Order1PrepareTime in min ,distance km /20kmhr -> in min)
        //cost2= cost from  R1 to C1 = distance/20
        //cost3 =cost from c1-> R2 min(cost1+cost2, Order2PrepareTime in min)
        //cost4 = cost from R2-> C2 = distance/20
//        Agent -> R1 -> C1 -> R2 -> C2
//        Agent -> R2 -> C2 -> R1 -> C1
//        Agent -> R1 -> R2 -> C1 -> C2
//        Agent -> R1 -> R2 -> C2 -> C1
//        Agent -> R2 -> R1 -> C1 -> C2
//        Agent -> R2 -> R1 -> C2 -> C1


        //        Agent -> R1 -> C1 -> R2 -> C2

        if (orders.size() >= 2) {
            //we will process only two orders at a time
            OrderEntry order1 = orders.get(0);
            OrderEntry order2 = orders.get(1);

            AllPossiblePathEntry trace = costSolver.calculateMinCost(agent, order1, order2);

            return trace;

        } else if (orders.size() == 1) {
            //then only one path possible
            //Agent->R1->C1
            OrderEntry order = orders.get(0);


            //agentToRestaurantCost= max of (order preparation time , agent location to restaurant traveling cost)
            //restaurantToCustomerCost => restaurant to customer traveling cost
            double agentToRestaurantCost = Math.max(TravelingCostCalculator.calculateTravelingCostInMinutes(distanceCalculatorContext.calculateDistance(agent.getLatitude(), agent.getLongitude(), order.getRestaurantLatitude(), order.getRestaurantLongitude()), Constant.Common.AVG_SPEED_OF_DELIVERY_AGENT), order.getMinTimeToPrepareInMinutes());

            double restaurantToCustomerCost = TravelingCostCalculator.calculateTravelingCostInMinutes(distanceCalculatorContext.calculateDistance(order.getRestaurantLatitude(), order.getRestaurantLongitude(), order.getUserLatitude(), order.getUserLongitude()), Constant.Common.AVG_SPEED_OF_DELIVERY_AGENT);

            double totalCost = agentToRestaurantCost + restaurantToCustomerCost;

            AllPossiblePathEntry trace = new AllPossiblePathEntry();

            List<String> path = Arrays.asList(agent.getName(), order.getRestaurantName(), order.getUserName());
            PathEntry minCostPath = new PathEntry();
            minCostPath.setPathCost(totalCost);
            minCostPath.setPath(path);

            trace.setMinCostPath(minCostPath);
            trace.setAllPossiblePath(Collections.singletonList(minCostPath));

            return trace;
        }


        return null;

    }
}
