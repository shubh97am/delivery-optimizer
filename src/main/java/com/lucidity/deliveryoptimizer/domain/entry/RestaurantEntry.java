package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lucidity.deliveryoptimizer.domain.entry.base.IDBaseEntry;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantEntry extends IDBaseEntry<Long> {

    private Long id;
    private String name;
    private String phone;
    private Boolean serviceable;
    private AddressEntry address;

    private Set<OrderEntry> orders;

}


