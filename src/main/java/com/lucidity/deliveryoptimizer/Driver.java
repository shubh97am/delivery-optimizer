package com.lucidity.deliveryoptimizer;

import com.lucidity.deliveryoptimizer.domain.entry.*;
import com.lucidity.deliveryoptimizer.domain.enumuration.Gender;
import com.lucidity.deliveryoptimizer.manager.DeliveryAgentManager;
import com.lucidity.deliveryoptimizer.manager.OrderFulfilmentService;
import com.lucidity.deliveryoptimizer.manager.RestaurantManager;
import com.lucidity.deliveryoptimizer.manager.UserManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Driver {


    private final UserManager userManager;
    private final RestaurantManager restaurantManager;
    private final DeliveryAgentManager deliveryAgentManager;
    private final OrderFulfilmentService orderFulfilmentService;

    public Driver(UserManager userManager, RestaurantManager restaurantManager, DeliveryAgentManager deliveryAgentManager, OrderFulfilmentService orderFulfilmentService) {
        this.userManager = userManager;
        this.restaurantManager = restaurantManager;
        this.deliveryAgentManager = deliveryAgentManager;
        this.orderFulfilmentService = orderFulfilmentService;
    }


    public AllPossiblePathEntry runner() {

        //setting up Customer1
        //@here change phone number every time in this request since there is uniqueness on phone number on user table
        UserEntry customer1 = new UserEntry();
        customer1.setName("Customer1");
        customer1.setPhone("917272727229");
        customer1.setGender(Gender.MALE);
        customer1.setAge(30);

        System.out.println("Creating Customer1");
        customer1 = userManager.createUser(customer1);
        System.out.println("Customer1 Created");

        System.out.println(customer1);

        AddressEntry customer1Address = new AddressEntry();
        customer1Address.setLatitude(13.9352);
        customer1Address.setLongitude(78.6221);
        customer1Address.setAddress("Dodanekundi");

        System.out.println("Adding Address for Customer1");
        customer1 = userManager.upsertUserAddress(customer1.getId(), customer1Address);
        System.out.println(" Address Added for Customer1");
        System.out.println(customer1);
        System.out.println();

        //setting up Customer2
        //@here change phone number every time in this request since there is uniqueness on phone number on user table
        UserEntry customer2 = new UserEntry();
        customer2.setName("Customer2");
        customer2.setPhone("918392929291");
        customer2.setGender(Gender.FEMALE);
        customer2.setAge(20);

        System.out.println("Creating Customer2");
        customer2 = userManager.createUser(customer2);
        System.out.println("Customer2 Created");

        System.out.println(customer2);

        AddressEntry customer2Address = new AddressEntry();
        customer2Address.setLatitude(12.9352);
        customer2Address.setLongitude(77.6244);
        customer2Address.setAddress("HSR LAYOUT");

        System.out.println("Adding Address for Customer2");
        customer2 = userManager.upsertUserAddress(customer2.getId(), customer2Address);
        System.out.println(" Address Added for Customer2");
        System.out.println(customer2);

        System.out.println();


        //here when we create restaurant is will be by default serviceable=true
        //if need to set serviceable=false then use updateRestaurantServiceability api

        //setting up Restaurant1
        RestaurantEntry restaurant1 = new RestaurantEntry();
        restaurant1.setName("Restaurant1");
        restaurant1.setPhone("+918890293232");

        AddressEntry restaurant1Address = new AddressEntry();
        restaurant1Address.setLatitude(14.9279);
        restaurant1Address.setLongitude(77.3271);
        restaurant1Address.setAddress("Indiranagar tin factory");
        restaurant1.setAddress(restaurant1Address);

        System.out.println("Creating Restaurant1");
        restaurant1 = restaurantManager.addRestaurant(restaurant1);
        System.out.println(" Restaurant1 Created Successfully");
        System.out.println(restaurant1);

        System.out.println();

        //setting up Restaurant2
        RestaurantEntry restaurant2 = new RestaurantEntry();
        restaurant2.setName("Restaurant2");
        restaurant2.setPhone("+918890293232");

        AddressEntry restaurant2Address = new AddressEntry();
        restaurant2Address.setLatitude(14.7878);
        restaurant2Address.setLongitude(77.2121);
        restaurant2Address.setAddress("Mahadevpura ");
        restaurant2.setAddress(restaurant2Address);

        System.out.println("Creating Restaurant2");
        restaurant2 = restaurantManager.addRestaurant(restaurant2);
        System.out.println(" Restaurant2 Created Successfully");
        System.out.println(restaurant2);

        System.out.println();


        //here when we create DeliveryAgent is will be by default serviceable(onDuty)=true
        //if need to set serviceable(onDuty)=false then use updateAgentOnDutyStatus api
        //create delivery agent Aman

        DeliveryAgentEntry agentEntry = new DeliveryAgentEntry();
        agentEntry.setName("Aman");
        agentEntry.setPhone("+917892929292");
        agentEntry.setGender(Gender.MALE);
        agentEntry.setLatitude(12.5555);
        agentEntry.setLatitude(77.3232);

        System.out.println("Creating Delivery Agent");

        agentEntry = deliveryAgentManager.addAgent(agentEntry);
        System.out.println(" Delivery Agent Created Successfully");

        System.out.println();


        //placing order1 for Customer1 from Restaurant1
        PlaceOrderInput placeOrder1 = new PlaceOrderInput();
        placeOrder1.setRestaurantId(restaurant1.getId());
        placeOrder1.setUserId(customer1.getId());
        placeOrder1.setMinTimeToPrepareInMin(30L);

        System.out.println("Placing Order1 by Customer1 from Restaurant2");
        OrderEntry order1 = orderFulfilmentService.placeOrder(placeOrder1);
        System.out.println("Order1 successfully placed");
        System.out.println(order1);

        System.out.println();

        //placing order1 for Customer2 from Restaurant2
        PlaceOrderInput placeOrder2 = new PlaceOrderInput();
        placeOrder2.setRestaurantId(restaurant2.getId());
        placeOrder2.setUserId(customer2.getId());
        placeOrder2.setMinTimeToPrepareInMin(60L);

        System.out.println("Placing Order2 by Customer2 from Restaurant2");
        OrderEntry order2 = orderFulfilmentService.placeOrder(placeOrder2);
        System.out.println("Order2 successfully placed");
        System.out.println(order2);

        System.out.println();


        //both orders will use this time as assign time since we want both orders to assigned at same time
        Date assignedOnTime = new Date();

        //assigning order 1
        AssignOrderInput assignOrderInput1 = new AssignOrderInput();
        assignOrderInput1.setOrderId(order1.getId());
        assignOrderInput1.setDeliveryAgentId(agentEntry.getId());
        assignOrderInput1.setAssignedOn(assignedOnTime);

        System.out.println("Assigning Order1 to Agent");
        OrderEntry assignOrder1 = orderFulfilmentService.assignOrder(assignOrderInput1);
        System.out.println("Order1 Assigned Successfully");

        System.out.println(assignOrder1);

        System.out.println();

        //assigning order 1
        AssignOrderInput assignOrderInput2 = new AssignOrderInput();
        assignOrderInput2.setOrderId(order2.getId());
        assignOrderInput2.setDeliveryAgentId(agentEntry.getId());
        assignOrderInput2.setAssignedOn(assignedOnTime);

        System.out.println("Assigning Order2 to Agent");
        OrderEntry assignOrder2 = orderFulfilmentService.assignOrder(assignOrderInput2);
        System.out.println("Order2 Assigned Successfully");

        System.out.println(assignOrder2);

        System.out.println();

        //finding min cost path

        OrdersDeliveryInput ordersDeliveryInput = new OrdersDeliveryInput();
        ordersDeliveryInput.setDeliveryAgentId(agentEntry.getId());
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(order1.getId());
        orderIds.add(order2.getId());
        ordersDeliveryInput.setOrderIds(orderIds);
        System.out.println("Finding Minimum Cost Path");
        AllPossiblePathEntry trace = orderFulfilmentService.calculateMinCostPath(ordersDeliveryInput);
        System.out.println("Path Found Successfully");

        System.out.println(trace);
        return trace;
    }
}
