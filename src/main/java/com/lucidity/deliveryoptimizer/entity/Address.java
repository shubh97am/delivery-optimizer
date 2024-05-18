package com.lucidity.deliveryoptimizer.entity;

import com.lucidity.deliveryoptimizer.common.mysql.entity.LongIDJPAEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

@Data
@Table(name = "address")
@NamedQueries(value = {
})
@Entity
public class Address extends LongIDJPAEntity {

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", id=" + id +
                '}';
    }
}
