package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lucidity.deliveryoptimizer.domain.entry.base.IDBaseEntry;
import com.lucidity.deliveryoptimizer.domain.enumuration.Gender;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryAgentEntry extends IDBaseEntry<Long> {
    private Long id;
    private String name;

    private Gender gender;
    private String phone;

    //current location of delivery Agent
    private Double latitude;
    private Double longitude;

    //this field tell us that if a user is on duty or not
    //if not then we won't assign new orders to that particular delivery agent
    private Boolean onDuty;

}
