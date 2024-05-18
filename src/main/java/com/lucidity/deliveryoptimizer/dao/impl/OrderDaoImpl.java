package com.lucidity.deliveryoptimizer.dao.impl;


import com.lucidity.deliveryoptimizer.common.mysql.dao.impl.CrudJPADAOImpl;
import com.lucidity.deliveryoptimizer.dao.OrderDao;
import com.lucidity.deliveryoptimizer.domain.enumuration.OrderStatus;
import com.lucidity.deliveryoptimizer.entity.Order;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDaoImpl extends CrudJPADAOImpl<Order, Long> implements OrderDao {
    @Override
    public Long getActiveOrderCountForDeliveryAgent(Long deliveryAgentId) {
        try {
            Query query = getEntityManager().createNamedQuery(Order.FETCH_ACTIVE_ORDERS_COUNT_FOR_DELIVERY_AGENT);
            query.setParameter("deliveryAgentId", deliveryAgentId);
            query.setParameter("assignedStatus", OrderStatus.ASSIGNED);
            query.setParameter("pickedStatus", OrderStatus.PICKED);
            return Optional.ofNullable(((Long) query.getSingleResult()).longValue()).orElse(0L);
        } catch (NoResultException e) {
            return 0L;
        }
    }

    @Override
    public List<Order> getActiveOrdersForAgent(Long deliveryAgentId) {
        try {
            Query query = getEntityManager().createNamedQuery(Order.FETCH_ACTIVE_ORDERS_FOR_DELIVERY_AGENT);
            query.setParameter("deliveryAgentId", deliveryAgentId);
            query.setParameter("assignedStatus", OrderStatus.ASSIGNED);
            query.setParameter("pickedStatus", OrderStatus.PICKED);
            return (List<Order>) query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}

