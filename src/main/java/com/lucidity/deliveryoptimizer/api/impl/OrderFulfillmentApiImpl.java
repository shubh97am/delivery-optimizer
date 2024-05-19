package com.lucidity.deliveryoptimizer.api.impl;

import com.lucidity.deliveryoptimizer.Driver;
import com.lucidity.deliveryoptimizer.api.OrderFulfillmentApi;
import com.lucidity.deliveryoptimizer.common.annotations.EnableLogging;
import com.lucidity.deliveryoptimizer.domain.entry.*;
import com.lucidity.deliveryoptimizer.domain.response.APIResponse;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import com.lucidity.deliveryoptimizer.manager.OrderFulfilmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFulfillmentApiImpl implements OrderFulfillmentApi {

    private final Driver driver;
    private final OrderFulfilmentService orderFulfilmentService;

    public OrderFulfillmentApiImpl(OrderFulfilmentService orderFulfilmentService, Driver driver) {
        this.orderFulfilmentService = orderFulfilmentService;
        this.driver = driver;
    }

    @Override
    @EnableLogging
    public ResponseEntity<Response> placeOrder(PlaceOrderInput input) {
        OrderEntry order = orderFulfilmentService.placeOrder(input);
        return APIResponse.renderSuccess(order, 200, HttpStatus.OK);
    }

    @Override
    @EnableLogging
    public ResponseEntity<Response> assignOrder(AssignOrderInput input) {
        OrderEntry order = orderFulfilmentService.assignOrder(input);
        return APIResponse.renderSuccess(order, 200, HttpStatus.OK);
    }

    @Override
    @EnableLogging
    public ResponseEntity<Response> getActiveOrdersForAgent(Long agentId) {
        List<OrderEntry> orders = orderFulfilmentService.getActiveOrdersForDeliveryAgent(agentId);
        return APIResponse.renderSuccess(orders, 200, HttpStatus.OK);
    }

    @Override
    @EnableLogging
    public ResponseEntity<Response> calculateMinCostPath(OrdersDeliveryInput input) {
        AllPossiblePathEntry tasks = orderFulfilmentService.calculateMinCostPath(input);
        return APIResponse.renderSuccess(tasks, 200, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> runner() {
        AllPossiblePathEntry tasks = driver.runner();
        return APIResponse.renderSuccess(tasks, 200, HttpStatus.OK);
    }
}
