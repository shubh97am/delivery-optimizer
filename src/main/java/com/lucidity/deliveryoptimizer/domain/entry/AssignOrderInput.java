package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignOrderInput {
    private Long orderId;
    private Long deliveryAgentId;


    //since we are solving problem for more than one orders assigned to agent at same time
    //so we will pick this time if present else current time
    private Date assignedOn;
}
