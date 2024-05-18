package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdersDeliveryInput {
    //in our case we are trying to solve only for two orders at particular time

    private Long deliveryAgentId;
    private List<Long> orderIds;
}
