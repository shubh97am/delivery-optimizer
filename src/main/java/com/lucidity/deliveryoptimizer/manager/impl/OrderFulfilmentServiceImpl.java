package com.lucidity.deliveryoptimizer.manager.impl;

import com.lucidity.deliveryoptimizer.common.constants.Constant;
import com.lucidity.deliveryoptimizer.common.util.MinPrepareTimeGenerator;
import com.lucidity.deliveryoptimizer.domain.entry.*;
import com.lucidity.deliveryoptimizer.domain.enumuration.OrderStatus;
import com.lucidity.deliveryoptimizer.manager.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderFulfilmentServiceImpl implements OrderFulfilmentService {

    private final RestaurantManager restaurantManager;
    private final UserManager userManager;
    private final DeliveryAgentManager deliveryAgentManager;

    private final OrderManager orderManager;

    public OrderFulfilmentServiceImpl(RestaurantManager restaurantManager, UserManager userManager, DeliveryAgentManager deliveryAgentManager, OrderManager orderManager) {
        this.restaurantManager = restaurantManager;
        this.userManager = userManager;
        this.deliveryAgentManager = deliveryAgentManager;
        this.orderManager = orderManager;
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
        if (input.getMinTimeToPrepareInMin() != null) {
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

    public OrderDeliveryFlowEntry calculateMinCostPath(OrdersDeliveryInput input) {

        if (input == null || input.getOrderIds() == null || input.getOrderIds().isEmpty() || input.getDeliveryAgentId() == null) {
            throw new RuntimeException("Empty Input for DeliveryTaskAssignment");
        }

        //here assuming that all orders assigned to deliveryAgent at the same time

        Map<Long, OrderEntry> orders = new HashMap<>();
        for (Long orderId : input.getOrderIds()) {
            OrderEntry orderEntry = orderManager.getOrder(orderId);
            if (orderEntry != null
                    && orderEntry.getDeliveryAgentId() != null
                    && orderEntry.getDeliveryAgentId().equals(input.getDeliveryAgentId())
                    && orderEntry.getOrderStatus() != null
                    && (OrderStatus.ASSIGNED == orderEntry.getOrderStatus() || OrderStatus.PICKED == orderEntry.getOrderStatus())) {
                orders.put(orderEntry.getId(), orderEntry);
            }
        }

        return null;

    }
}
