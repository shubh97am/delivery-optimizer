package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDeliveryFlowEntry {
    private Double totalCost;
    private List<Flow> taskFlow;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Flow {
        private String source;
        private String destination;
        private Double cost;
        private Date startingTime;
        private Date finishTime;

        @Override
        public String toString() {
            return "Flow{" +
                    "source='" + source + '\'' +
                    ", destination='" + destination + '\'' +
                    ", cost=" + cost +
                    ", startingTime=" + startingTime +
                    ", finishTime=" + finishTime +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OrderDeliveryFlowEntry{" +
                "totalCost=" + totalCost +
                ", taskFlow=" + taskFlow +
                '}';
    }
}
