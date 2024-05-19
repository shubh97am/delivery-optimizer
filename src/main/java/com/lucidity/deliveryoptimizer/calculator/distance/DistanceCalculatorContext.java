package com.lucidity.deliveryoptimizer.calculator.distance;

public class DistanceCalculatorContext {
    private DistanceCalculatorStrategy strategy;

    public DistanceCalculatorContext(DistanceCalculatorStrategy sortingStrategy) {
        this.strategy = sortingStrategy;
    }

    public void setSortingStrategy(DistanceCalculatorStrategy sortingStrategy) {
        this.strategy = sortingStrategy;
    }

    public double calculateDistance(double lat1, double long1, double lat2, double long2) {
        return strategy.calculateDistance(lat1, long1, lat2, long2);
    }
}
