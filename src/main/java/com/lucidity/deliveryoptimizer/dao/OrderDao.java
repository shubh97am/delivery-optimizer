package com.lucidity.deliveryoptimizer.dao;

import com.lucidity.deliveryoptimizer.common.mysql.dao.jpa.CrudJPADAO;
import com.lucidity.deliveryoptimizer.entity.Order;

import java.util.List;

public interface OrderDao extends CrudJPADAO<Order, Long> {

    Long getActiveOrderCountForDeliveryAgent(Long deliveryAgentId);

    List<Order> getActiveOrdersForAgent(Long deliveryAgentId);

}