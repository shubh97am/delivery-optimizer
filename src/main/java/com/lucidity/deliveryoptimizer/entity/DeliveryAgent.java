package com.lucidity.deliveryoptimizer.entity;

import com.lucidity.deliveryoptimizer.common.mysql.entity.LongIDJPAEntity;
import com.lucidity.deliveryoptimizer.domain.enumuration.Gender;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "delivery_agent")
@NamedQueries(value = {
})
@Entity
public class DeliveryAgent extends LongIDJPAEntity {

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    //current Address of delivery Agent
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    //this field tell us that if a user is on duty or not
    //if not then we won't assign new orders to that particular delivery agent
    @Column(name = "on_duty")
    private Boolean onDuty;


    //todo we can add more details here for delivery Agent like DOB

}
