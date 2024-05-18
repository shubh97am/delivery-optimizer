package com.lucidity.deliveryoptimizer.manager.impl;

import com.lucidity.deliveryoptimizer.common.exception.InvalidInputRequestException;
import com.lucidity.deliveryoptimizer.common.util.BeanTransformUtil;
import com.lucidity.deliveryoptimizer.dao.OrderDao;
import com.lucidity.deliveryoptimizer.domain.entry.OrderEntry;
import com.lucidity.deliveryoptimizer.domain.enumuration.OrderStatus;
import com.lucidity.deliveryoptimizer.entity.Order;
import com.lucidity.deliveryoptimizer.manager.OrderManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderManagerImpl implements OrderManager {

    private final OrderDao orderDao;

    public OrderManagerImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public OrderEntry createOrder(OrderEntry input) {
        validateOrderCreateRequest(input);
        Order entity = convertToEntity(input, null);
        entity = orderDao.create(entity);
        return convertToEntry(entity);
    }

    @Override
    public OrderEntry getOrder(Long id) {
        if (id == null) {
            throw new RuntimeException("id not provided");
        }

        Order order = orderDao.findById(id);
        return convertToEntry(order);
    }

    @Override
    public OrderEntry updateOrder(Long id, OrderEntry input) {
        if (id == null || input == null) {
            throw new RuntimeException("Invalid input while updating order");
        }

        Order entity = orderDao.findById(id);

        if (entity == null) {
            throw new RuntimeException("Order not found for id=" + id);
        }

        validateOrderStatusUpdate(input, entity);

        entity = convertToEntity(input, entity);

        entity = orderDao.update(id, entity);

        return convertToEntry(entity);

    }

    @Override
    public Long getActiveOrdersCountForAgent(Long deliveryAgentId) {
        if (deliveryAgentId == null) {
            return 0L;
        }
        return orderDao.getActiveOrderCountForDeliveryAgent(deliveryAgentId);
    }

    @Override
    public List<OrderEntry> getActiveOrdersForAgent(Long agentId) {
        if (agentId == null) {
            throw new InvalidInputRequestException("agent id missing in input");
        }
        List<Order> orders = orderDao.getActiveOrdersForAgent(agentId);
        return convertToEntryList(orders);
    }

    private void validateOrderStatusUpdate(OrderEntry entry, Order entity) {
        if (entity == null || entry == null) {
            return;
        }

        OrderStatus existingStatus = entity.getOrderStatus();
        OrderStatus statusToUpdate = entry.getOrderStatus();

        if (existingStatus == null || statusToUpdate == null) {
            return;
        }
        if (statusToUpdate == OrderStatus.ASSIGNED) {
            if (existingStatus == OrderStatus.PICKED || existingStatus == OrderStatus.DELIVERED) {
                throw new RuntimeException("Invalid Order Status Transition");
            }
        } else if (statusToUpdate == OrderStatus.PICKED) {
            if (existingStatus == OrderStatus.DELIVERED) {
                throw new RuntimeException("Invalid Order Status Transition");
            }
        }
    }

    private void validateOrderCreateRequest(OrderEntry entry) {
        if (entry == null
                || entry.getRestaurantId() == null
                || StringUtils.isEmpty(entry.getRestaurantName())
                || StringUtils.isEmpty(entry.getRestaurantPhone())
                || StringUtils.isEmpty(entry.getRestaurantAddress())
                || entry.getRestaurantLatitude() == null
                || entry.getRestaurantLongitude() == null
                || entry.getMinTimeToPrepareInMinutes() == null) {
            throw new RuntimeException("Restaurant Meta Data Missing While Creating Order");
        }

        if (entry.getUserId() == null
                || StringUtils.isEmpty(entry.getUserName())
                || StringUtils.isEmpty(entry.getUserPhone())
                || StringUtils.isEmpty(entry.getUserAddress())
                || entry.getUserLatitude() == null
                || entry.getUserLongitude() == null) {
            throw new RuntimeException("Restaurant Meta Data Missing While Creating Order");
        }
    }

    private Order convertToEntity(OrderEntry entry, Order entity) {
        if (entry == null) {
            return entity;
        }

        if (entity == null) {
            entity = new Order();
            //setting restaurant/user metadata on order
            // so even id anything changed related to name,phone,address
            // we will pick details what are there at the time of order placement
            entity.setRestaurantId(entry.getRestaurantId());
            entity.setRestaurantName(entry.getRestaurantName());
            entity.setRestaurantPhone(entry.getRestaurantPhone());
            entity.setRestaurantAddress(entry.getRestaurantAddress());
            entity.setRestaurantLatitude(entry.getRestaurantLatitude());
            entity.setRestaurantLongitude(entry.getRestaurantLongitude());
            entity.setMinTimeToPrepareInMinutes(entry.getMinTimeToPrepareInMinutes());

            entity.setUserId(entry.getUserId());
            entity.setUserName(entry.getUserName());
            entity.setUserPhone(entry.getUserPhone());
            entity.setUserAddress(entry.getUserAddress());
            entity.setUserLatitude(entry.getUserLatitude());
            entity.setUserLongitude(entry.getUserLongitude());


            //initial status
            entity.setOrderStatus(OrderStatus.CREATED);


        }

        if (entry.getOrderStatus() != null) {
            entity.setOrderStatus(entry.getOrderStatus());
        }

        if (entry.getAssignedOn() != null) {
            entity.setAssignedOn(entry.getAssignedOn());
        }
        if (entry.getPickedOn() != null) {
            entity.setPickedOn(entry.getPickedOn());
        }
        if (entry.getDeliveredOn() != null) {
            entity.setDeliveredOn(entry.getDeliveredOn());
        }
        if (entry.getDeliveryAgentId() != null) {
            entity.setDeliveryAgentId(entry.getDeliveryAgentId());
        }

        return entity;
    }


    private OrderEntry convertToEntry(Order entity) {
        if (entity == null) {
            return null;
        }

        OrderEntry entry = new OrderEntry();
        entry.setId(entity.getId());
        BeanTransformUtil.copyProperties(entity, entry);
        return entry;
    }

    private List<OrderEntry> convertToEntryList(List<Order> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }

        List<OrderEntry> entries = new ArrayList<>();
        for (Order entity : entities) {
            if (entity != null) {
                entries.add(convertToEntry(entity));
            }
        }
        return entries;
    }
}
