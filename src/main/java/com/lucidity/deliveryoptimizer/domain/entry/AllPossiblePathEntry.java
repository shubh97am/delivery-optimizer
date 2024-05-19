package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllPossiblePathEntry {
    private PathEntry minCostPath;
    private List<PathEntry> allPossiblePath;

    @Override
    public String toString() {
        return "AllPossiblePathEntry{" +
                "minCostPath=" + minCostPath +
                ", allPossiblePath=" + allPossiblePath +
                '}';
    }
}
