package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lucidity.deliveryoptimizer.domain.entry.base.IDBaseEntry;
import com.lucidity.deliveryoptimizer.domain.enumuration.OrderStatus;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEntry extends IDBaseEntry<Long> {

    private Long id;

    private Long restaurantId;

    private String restaurantName;

    private String restaurantPhone;

    private String restaurantAddress;

    private Double restaurantLatitude;

    private Double restaurantLongitude;

    private Long minTimeToPrepareInMinutes;

    private Long userId;

    private String userName;

    private String userPhone;

    private String userAddress;

    private Double userLatitude;

    private Double userLongitude;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Date assignedOn;

    private Date pickedOn;

    private Date deliveredOn;

    private Long deliveryAgentId;

    @Override
    public String toString() {
        return "OrderEntry{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantPhone='" + restaurantPhone + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", restaurantLatitude=" + restaurantLatitude +
                ", restaurantLongitude=" + restaurantLongitude +
                ", minTimeToPrepareInMinutes=" + minTimeToPrepareInMinutes +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userLatitude=" + userLatitude +
                ", userLongitude=" + userLongitude +
                ", orderStatus=" + orderStatus +
                ", assignedOn=" + assignedOn +
                ", pickedOn=" + pickedOn +
                ", deliveredOn=" + deliveredOn +
                ", deliveryAgentId=" + deliveryAgentId +
                '}';
    }
}
