package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceOrderInput {
    private Long userId;
    private Long restaurantId;

    private Long minTimeToPrepareInMin;

    //here we can take more inputs as item details and all but that is out of scope for now
    //we can take total amount in input to validate cart while placing order but that is out of scope for now
}
