package com.lucidity.deliveryoptimizer.manager.impl;




import java.util.*;
import java.util.*;

class Location {
    String name;
    double latitude;
    double longitude;
    double prepTime; // Preparation time for restaurant, 0 for consumers

    public Location(String name, double latitude, double longitude, double prepTime) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prepTime = prepTime;
    }
}

class Edge {
    Location source;
    Location destination;
    double distance;

    public Edge(Location source, Location destination, double distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }
}

public class PossibleSolution2 {

    public static void main(String[] args) {
        // Define locations
        Location agent = new Location("Agent", 0, 0, 0);
        Location consumer1 = new Location("Consumer 1", 12, 34, 0);
        Location consumer2 = new Location("Consumer 2", 56, 78, 0);
        Location restaurant1 = new Location("Restaurant 1", 23, 45, 10); // Assuming 10 mins prep time
        Location restaurant2 = new Location("Restaurant 2", 67, 89, 15); // Assuming 15 mins prep time

        // Define edges (connections between locations) and their distances
        Edge[] edges = {
                new Edge(agent, restaurant1, calculateDistance(agent, restaurant1)),
                new Edge(restaurant1, consumer1, calculateDistance(restaurant1, consumer1)),
                new Edge(agent, restaurant2, calculateDistance(agent, restaurant2)),
                new Edge(restaurant2, consumer2, calculateDistance(restaurant2, consumer2)),
                new Edge(consumer1, consumer2, calculateDistance(consumer1, consumer2))
        };

        // Find shortest path
        List<Location> path = findShortestPath(agent, consumer1, consumer2, edges);

        // Print the path
        System.out.println("Shortest path:");
        for (Location loc : path) {
            System.out.println(loc.name);
        }
    }

    public static List<Location> findShortestPath(Location source, Location destination1, Location destination2, Edge[] edges) {
        Map<Location, Double> distances = new HashMap<>();
        Map<Location, Location> previous = new HashMap<>();
        Map<Location, Boolean> order1Delivered = new HashMap<>(); // Keep track of whether Order 1 is delivered

        for (Edge edge : edges) {
            distances.put(edge.source, Double.MAX_VALUE);
            distances.put(edge.destination, Double.MAX_VALUE);
            previous.put(edge.source, null);
            previous.put(edge.destination, null);
            order1Delivered.put(edge.source, false);
            order1Delivered.put(edge.destination, false);
        }

        distances.put(source, 0.0);

        PriorityQueue<Location> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        queue.add(source);

        while (!queue.isEmpty()) {
            Location current = queue.poll();
            for (Edge edge : edges) {
                if (edge.source.equals(current)) {
                    double newDistance = distances.get(current) + edge.distance + edge.destination.prepTime;
                    if (order1Delivered.get(current) || edge.destination == destination1 || edge.destination == destination2) {
                        // If Order 1 is delivered, or destination is destination1 or destination2
                        if (newDistance < distances.get(edge.destination)) {
                            distances.put(edge.destination, newDistance);
                            previous.put(edge.destination, current);
                            if (edge.destination == destination1) {
                                order1Delivered.put(edge.destination, true);
                            }
                            queue.add(edge.destination);
                        }
                    } else {
                        // If Order 1 is not delivered and destination is not destination1 or destination2
                        if (newDistance - edge.destination.prepTime < distances.get(edge.destination)) {
                            distances.put(edge.destination, newDistance - edge.destination.prepTime);
                            previous.put(edge.destination, current);
                            queue.add(edge.destination);
                        }
                    }
                }
            }
        }

        return getPath(source, destination1, destination2, previous);
    }

    public static List<Location> getPath(Location source, Location destination1, Location destination2, Map<Location, Location> previous) {
        List<Location> path = new ArrayList<>();
        Location destination = destination1;
        while (destination != null) {
            path.add(destination);
            destination = previous.get(destination);
            if (destination == source) {
                break;
            }
        }
        Collections.reverse(path);
        if (path.contains(destination2)) {
            // If destination2 is already reached, remove it from the path to avoid duplicates
            path.remove(destination2);
        }
        // Add the path to destination2
        path.addAll(getPath(source, null,destination2, previous).subList(1, getPath(source,null, destination2, previous).size()));
        return path;
    }

    public static double calculateDistance(Location loc1, Location loc2) {
        double lat1 = loc1.latitude;
        double lon1 = loc1.longitude;
        double lat2 = loc2.latitude;
        double lon2 = loc2.longitude;
        double earthRadius = 6371; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c; // Distance in kilometers
        return distance;
    }
}
