package com.lucidity.deliveryoptimizer.calculator.distance;

public interface DistanceCalculatorStrategy {
    public double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2);
}
