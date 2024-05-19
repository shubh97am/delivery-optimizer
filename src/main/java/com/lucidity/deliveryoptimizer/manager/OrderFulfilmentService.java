package com.lucidity.deliveryoptimizer.manager;

import com.lucidity.deliveryoptimizer.domain.entry.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderFulfilmentService {

    @Transactional(rollbackFor = Exception.class)
    public OrderEntry placeOrder(PlaceOrderInput input);

    @Transactional(rollbackFor = Exception.class)
    public OrderEntry assignOrder(AssignOrderInput input);

    @Transactional(readOnly = true)
    public List<OrderEntry> getActiveOrdersForDeliveryAgent(Long agentId);

    @Transactional(rollbackFor = Exception.class)
    public OrderDeliveryFlowEntry calculateMinCostPath(OrdersDeliveryInput input);
}
