package com.lucidity.deliveryoptimizer.api;

import com.lucidity.deliveryoptimizer.common.constants.Constant;
import com.lucidity.deliveryoptimizer.domain.entry.AssignOrderInput;
import com.lucidity.deliveryoptimizer.domain.entry.OrdersDeliveryInput;
import com.lucidity.deliveryoptimizer.domain.entry.PlaceOrderInput;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.Controller.BASE_URL)
public interface OrderFulfillmentApi {

    @PostMapping(Constant.OrderFulfillmentController.PLACE_ORDER)
    public ResponseEntity<Response> placeOrder(@RequestBody PlaceOrderInput input);

    @PostMapping(Constant.OrderFulfillmentController.ASSIGN_ORDER)
    public ResponseEntity<Response> assignOrder(@RequestBody AssignOrderInput input);


    @GetMapping(Constant.OrderFulfillmentController.GET_ACTIVE_ORDERS_FOR_AGENT)
    public ResponseEntity<Response> getActiveOrdersForAgent(@PathVariable("agentId") Long agentId);

    @PostMapping(Constant.OrderFulfillmentController.EXECUTE_DELIVERY_TASK)
    public ResponseEntity<Response> executeDeliveryTask(@RequestBody OrdersDeliveryInput input);
}
