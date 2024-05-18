package com.lucidity.deliveryoptimizer.entity;


import com.lucidity.deliveryoptimizer.common.mysql.entity.LongIDJPAEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

@Data
@Table(name = "restaurant")
@NamedQueries(value = {
})
@Entity
public class Restaurant extends LongIDJPAEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_serviceable")
    private Boolean serviceable;

    @Column(name = "address_id")
    private Long addressId;

    //todo check if avg time to prepare order on order level or res level

}
