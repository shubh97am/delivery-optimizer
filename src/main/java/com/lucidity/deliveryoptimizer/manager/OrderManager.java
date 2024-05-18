package com.lucidity.deliveryoptimizer.manager;

import com.lucidity.deliveryoptimizer.domain.entry.OrderEntry;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderManager {

    @Transactional(rollbackFor = Exception.class)
    OrderEntry createOrder(OrderEntry input);

    @Transactional(readOnly = true)
    OrderEntry getOrder(Long id);

    @Transactional(rollbackFor = Exception.class)
    OrderEntry updateOrder(Long id, OrderEntry input);

    //this will return active orders for delivery agent
    //active order means assign or picked but not delivered at;
    @Transactional(readOnly = true)
    Long getActiveOrdersCountForAgent(Long deliveryAgentId);

    @Transactional(readOnly = true)
    List<OrderEntry> getActiveOrdersForAgent(Long agentId);


    //we can add more methods here
    // get all orders with filters(for user/for restaurant/by status/ by assignedDeliveryAgent);
}
