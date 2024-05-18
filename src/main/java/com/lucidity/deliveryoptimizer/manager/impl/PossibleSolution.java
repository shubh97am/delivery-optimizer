package com.lucidity.deliveryoptimizer.manager.impl;


import java.util.*;

public class PossibleSolution {

    static class Location {
        String name;
        double latitude;
        double longitude;
        double mealPrepTime;

        public Location(String name, double latitude, double longitude, double mealPrepTime) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.mealPrepTime = mealPrepTime;
        }
    }

    static class Edge {
        Location target;
        double weight;

        public Edge(Location target, double weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    static class Graph {
        Map<Location, List<Edge>> adjList = new HashMap<>();

        public void addLocation(Location location) {
            adjList.putIfAbsent(location, new ArrayList<>());
        }

        public void addEdge(Location source, Location destination, double weight) {
            adjList.get(source).add(new Edge(destination, weight));
        }
    }

    // Haversine formula to calculate distance between two geo-locations
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the Earth in kilometers
        double R = 6371;

        // Convert latitude and longitude from degrees to radians
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        // Calculate the square of half the chord length between the points
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        // Calculate the angular distance in radians
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance
        double distance = R * c;

        return distance;
    }

    // Dijkstra's algorithm to find shortest path
    public static double dijkstra(Graph graph, Location start, Location end) {
        Map<Location, Double> distance = new HashMap<>();
        PriorityQueue<Location> pq = new PriorityQueue<>(Comparator.comparingDouble(distance::get));

        // Initialize distances
        for (Location location : graph.adjList.keySet()) {
            distance.put(location, Double.MAX_VALUE);
        }
        distance.put(start, 0.0);

        pq.offer(start);

        while (!pq.isEmpty()) {
            Location current = pq.poll();
            if (current == end) break;

            for (Edge edge : graph.adjList.get(current)) {
                double newDist = distance.get(current) + edge.weight + edge.target.mealPrepTime;
                if (newDist < distance.get(edge.target)) {
                    distance.put(edge.target, newDist);
                    pq.offer(edge.target);
                }
            }
        }

        return distance.get(end);
    }

    public static void main(String[] args) {
        Location aman = new Location("Aman", 12.9279, 77.6271, 0);
        Location consumer1 = new Location("C1", 12.9352, 77.6245, 0);
        Location consumer2 = new Location("C2", 12.9284, 77.6260, 0);
        Location restaurant1 = new Location("R1", 12.9316, 77.6221, 0.5); // Example meal prep time for R1
        Location restaurant2 = new Location("R2", 12.9299, 77.6265, 0.7); // Example meal prep time for R2

        Graph graph = new Graph();
        graph.addLocation(aman);
        graph.addLocation(consumer1);
        graph.addLocation(consumer2);
        graph.addLocation(restaurant1);
        graph.addLocation(restaurant2);

        graph.addEdge(aman, restaurant1, haversine(aman.latitude, aman.longitude, restaurant1.latitude, restaurant1.longitude));
        graph.addEdge(aman, restaurant2, haversine(aman.latitude, aman.longitude, restaurant2.latitude, restaurant2.longitude));
        graph.addEdge(restaurant1, consumer1, haversine(restaurant1.latitude, restaurant1.longitude, consumer1.latitude, consumer1.longitude));
        graph.addEdge(restaurant2, consumer2, haversine(restaurant2.latitude, restaurant2.longitude, consumer2.latitude, consumer2.longitude));

        double shortestTimeToDeliver = Math.min(dijkstra(graph, aman, consumer1), dijkstra(graph, aman, consumer2));
        System.out.println("Shortest possible time to deliver the batch: " + shortestTimeToDeliver + " hours");
    }
}


//
//    To solve this problem optimally using a graph-based approach, we can model the scenario as a weighted directed graph where each node represents a location (consumer or restaurant) and each edge represents the travel time between two locations. We can then use a shortest path algorithm like Dijkstra's algorithm to find the shortest path from Aman's current location to each restaurant, and then to each consumer, considering the meal preparation times.
//
//        Here's the Java code implementing this approach:
//
//
//
//
//        This code defines a Graph class to represent the delivery network and uses Dijkstra's algorithm to find the shortest path from Aman's current location to each consumer, considering travel time and meal preparation time at restaurants. Adjust the location coordinates and meal preparation times accordingly.


