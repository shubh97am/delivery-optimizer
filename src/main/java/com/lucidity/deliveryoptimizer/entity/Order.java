package com.lucidity.deliveryoptimizer.entity;

import com.lucidity.deliveryoptimizer.common.mysql.entity.LongIDJPAEntity;
import com.lucidity.deliveryoptimizer.domain.enumuration.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "order")
@NamedQueries(value = {
        @NamedQuery(name = Order.FETCH_ACTIVE_ORDERS_COUNT_FOR_DELIVERY_AGENT, query = "select count(*) from Order where deliveryAgentId =:deliveryAgentId and (orderStatus=:assignedStatus or orderStatus=:pickedStatus)"),
        @NamedQuery(name = Order.FETCH_ACTIVE_ORDERS_FOR_DELIVERY_AGENT, query = "from Order where deliveryAgentId =:deliveryAgentId and (orderStatus=:assignedStatus or orderStatus=:pickedStatus)"),
})
@Entity
public class Order extends LongIDJPAEntity {

    public static final String FETCH_ACTIVE_ORDERS_COUNT_FOR_DELIVERY_AGENT = "FETCH_ACTIVE_ORDERS_COUNT_FOR_DELIVERY_AGENT";

    public static final String FETCH_ACTIVE_ORDERS_FOR_DELIVERY_AGENT = "FETCH_ACTIVE_ORDERS_FOR_DELIVERY_AGENT";


    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "restaurant_phone")
    private String restaurantPhone;

    @Column(name = "restaurant_address")
    private String restaurantAddress;

    @Column(name = "restaurant_latitude")
    private Double restaurantLatitude;

    @Column(name = "restaurant_longitude")
    private Double restaurantLongitude;

    @Column(name = "min_time_to_prepare_in_minutes")
    private Long minTimeToPrepareInMinutes;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_latitude")
    private Double userLatitude;

    @Column(name = "user_longitude")
    private Double userLongitude;


    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "assigned_on")
    private Date assignedOn;

    @Column(name = "picked_on")
    private Date pickedOn;

    @Column(name = "delivered_on")
    private Date deliveredOn;

    @Column(name = "delivery_agent_id")
    private Long deliveryAgentId;
}
