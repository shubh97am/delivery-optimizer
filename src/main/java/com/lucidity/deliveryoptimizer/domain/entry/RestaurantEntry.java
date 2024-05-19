package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lucidity.deliveryoptimizer.domain.entry.base.IDBaseEntry;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantEntry extends IDBaseEntry<Long> {

    private Long id;
    private String name;
    private String phone;
    private Boolean serviceable;
    private AddressEntry address;

    @Override
    public String toString() {
        return "RestaurantEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", serviceable=" + serviceable +
                ", address=" + address +
                '}';
    }
}


