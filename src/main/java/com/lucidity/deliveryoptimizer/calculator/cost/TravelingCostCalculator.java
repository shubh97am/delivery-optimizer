package com.lucidity.deliveryoptimizer.calculator.cost;

public class TravelingCostCalculator {

    public static double calculateTravelingCostInMinutes(double travelKms, double speedInKMPH) {

        double timeTakenInHours = travelKms / speedInKMPH;

        return (double) timeTakenInHours / 60;

    }
}
