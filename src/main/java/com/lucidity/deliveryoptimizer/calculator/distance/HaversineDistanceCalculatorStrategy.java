package com.lucidity.deliveryoptimizer.calculator.distance;

public class HaversineDistanceCalculatorStrategy implements DistanceCalculatorStrategy {
    @Override
    public double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(latitude2 - latitude1);
        double dLon = Math.toRadians(longitude2 - longitude1);

        // convert to radians
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(latitude1) *
                        Math.cos(latitude2);
        double rad = 6371; // radius of earth in KM
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

//    public static void main(String[] args) {
//        HaversineDistanceCalculatorStrategy obj = new HaversineDistanceCalculatorStrategy();
//        double lat2 = 51.5007;
//        double lon2 = 0.1246;
//        double lat1 = 40.6892;
//        double lon1 = 74.0445;
//        System.out.println(obj.calculateDistance(lat1, lon1, lat2, lon2) + " K.M.");
//    }

}
