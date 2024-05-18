package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lucidity.deliveryoptimizer.domain.entry.base.IDBaseEntry;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressEntry extends IDBaseEntry<Long> {
    private Long id;
    private String address;
    private Double latitude;
    private Double longitude;

    @Override
    public String toString() {
        return "AddressEntry{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
