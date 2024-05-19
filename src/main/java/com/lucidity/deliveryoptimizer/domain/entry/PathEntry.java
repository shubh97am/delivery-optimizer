package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PathEntry {
    double pathCost;
    List<String> path;

    @Override
    public String toString() {
        return "PathEntry{" +
                "pathCost=" + pathCost +
                ", path=" + path +
                '}';
    }
}
