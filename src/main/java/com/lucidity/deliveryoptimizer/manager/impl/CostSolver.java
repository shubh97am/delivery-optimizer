package com.lucidity.deliveryoptimizer.manager.impl;


import com.lucidity.deliveryoptimizer.calculator.distance.DistanceCalculatorContext;
import com.lucidity.deliveryoptimizer.calculator.distance.HaversineDistanceCalculatorStrategy;
import com.lucidity.deliveryoptimizer.common.constants.Constant;
import com.lucidity.deliveryoptimizer.domain.entry.AllPossiblePathEntry;
import com.lucidity.deliveryoptimizer.domain.entry.DeliveryAgentEntry;
import com.lucidity.deliveryoptimizer.domain.entry.OrderEntry;
import com.lucidity.deliveryoptimizer.domain.entry.PathEntry;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CostSolver {
    @Data
    static class Location {
        String name;
        double latitude;
        double longitude;
        long prepareTime;

        public Location(String name, double latitude, double longitude, long prepareTime) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.prepareTime = prepareTime;
        }
    }

    @Data
    static class Edge {
        String destination;
        double distance;

        public Edge(String destination, double distance) {
            this.destination = destination;
            this.distance = distance;
        }
    }

    public AllPossiblePathEntry calculateMinCost(DeliveryAgentEntry agent, OrderEntry order1, OrderEntry order2) {

        Map<String, Location> locations = new HashMap<>();
//        locations.put("Aman", new Location("Aman", lat_Aman, lon_Aman, 0));
//        locations.put("R1", new Location("R1", lat_R1, lon_R1, preparationTime1));
//        locations.put("R2", new Location("R2", lat_R2, lon_R2, preparationTime2));
//        locations.put("C1", new Location("C1", lat_C1, lon_C1, 0));
//        locations.put("C2", new Location("C2", lat_C2, lon_C2, 0));

        locations.put(agent.getName(), new Location(agent.getName(), agent.getLatitude(), agent.getLongitude(), 0));
        locations.put(order1.getRestaurantName(), new Location(order1.getRestaurantName(), order1.getRestaurantLatitude(), order1.getRestaurantLongitude(), order1.getMinTimeToPrepareInMinutes()));
        locations.put(order2.getRestaurantName(), new Location(order2.getRestaurantName(), order2.getRestaurantLatitude(), order2.getRestaurantLongitude(), order2.getMinTimeToPrepareInMinutes()));
        locations.put(order1.getUserName(), new Location(order1.getUserName(), order1.getUserLatitude(), order1.getUserLongitude(), 0));
        locations.put(order2.getUserName(), new Location(order2.getUserName(), order2.getUserLatitude(), order2.getUserLongitude(), 0));

        // Define the edges representing travel times
        Map<String, List<Edge>> graph = new HashMap<>();
//        graph.put("Aman", Arrays.asList(new Edge("R1", haversineDistance(locations.get("Aman"), locations.get("R1"))),
//                new Edge("R2", haversineDistance(locations.get("Aman"), locations.get("R2")))));
//        graph.put("R1", Arrays.asList(new Edge("C1", haversineDistance(locations.get("R1"), locations.get("C1"))),
//                new Edge("R2", haversineDistance(locations.get("R1"), locations.get("R2"))),
//                new Edge("C2", haversineDistance(locations.get("R1"), locations.get("C2")))));
//        graph.put("R2", Arrays.asList(new Edge("C1", haversineDistance(locations.get("R2"), locations.get("C1"))),
//                new Edge("R1", haversineDistance(locations.get("R2"), locations.get("R1"))),
//                new Edge("C2", haversineDistance(locations.get("R2"), locations.get("C2")))));
//        graph.put("C1", Arrays.asList(new Edge("R2", haversineDistance(locations.get("C1"), locations.get("R2"))),
//                new Edge("C2", haversineDistance(locations.get("C1"), locations.get("C2")))));
//        graph.put("C2", Arrays.asList(new Edge("R1", haversineDistance(locations.get("C2"), locations.get("R1"))),
//                new Edge("C1", haversineDistance(locations.get("C2"), locations.get("C1")))));


        graph.put(agent.getName(), Arrays.asList(new Edge(order1.getRestaurantName(), calculateDistance(locations.get(agent.getName()), locations.get(order1.getRestaurantName()))),
                new Edge(order2.getRestaurantName(), calculateDistance(locations.get(agent.getName()), locations.get(order2.getRestaurantName())))));

        graph.put(order1.getRestaurantName(), Arrays.asList(new Edge(order1.getUserName(), calculateDistance(locations.get(order1.getRestaurantName()), locations.get(order1.getUserName()))),
                new Edge(order2.getRestaurantName(), calculateDistance(locations.get(order1.getRestaurantName()), locations.get(order2.getRestaurantName()))),
                new Edge(order2.getUserName(), calculateDistance(locations.get(order1.getRestaurantName()), locations.get(order2.getUserName())))));

        graph.put(order2.getRestaurantName(), Arrays.asList(new Edge(order1.getUserName(), calculateDistance(locations.get(order2.getRestaurantName()), locations.get(order1.getUserName()))),
                new Edge(order1.getRestaurantName(), calculateDistance(locations.get(order2.getRestaurantName()), locations.get(order1.getRestaurantName()))),
                new Edge(order2.getUserName(), calculateDistance(locations.get(order2.getRestaurantName()), locations.get(order2.getUserName())))));

        graph.put(order1.getUserName(), Arrays.asList(new Edge(order2.getRestaurantName(), calculateDistance(locations.get(order1.getUserName()), locations.get(order2.getRestaurantName()))),
                new Edge(order2.getUserName(), calculateDistance(locations.get(order1.getUserName()), locations.get(order2.getUserName())))));

        graph.put(order2.getUserName(), Arrays.asList(new Edge(order1.getRestaurantName(), calculateDistance(locations.get(order2.getUserName()), locations.get(order1.getRestaurantName()))),
                new Edge(order1.getUserName(), calculateDistance(locations.get(order2.getUserName()), locations.get(order1.getUserName())))));


        // Find the shortest path for each permutation
//        List<List<String>> permutations = Arrays.asList(
//                Arrays.asList("Aman", "R1", "C1", "R2", "C2"),
//                Arrays.asList("Aman", "R2", "C2", "R1", "C1"),
//                Arrays.asList("Aman", "R1", "R2", "C1", "C2"),
//                Arrays.asList("Aman", "R1", "R2", "C2", "C1"),
//                Arrays.asList("Aman", "R2", "R1", "C1", "C2"),
//                Arrays.asList("Aman", "R2", "R1", "C2", "C1")
//        );

        List<List<String>> permutations = Arrays.asList(
                Arrays.asList(agent.getName(), order1.getRestaurantName(), order1.getUserName(), order2.getRestaurantName(), order2.getUserName()),
                Arrays.asList(agent.getName(), order2.getRestaurantName(), order2.getUserName(), order1.getRestaurantName(), order1.getUserName()),
                Arrays.asList(agent.getName(), order1.getRestaurantName(), order2.getRestaurantName(), order1.getUserName(), order2.getUserName()),
                Arrays.asList(agent.getName(), order1.getRestaurantName(), order2.getRestaurantName(), order2.getUserName(), order1.getUserName()),
                Arrays.asList(agent.getName(), order2.getRestaurantName(), order1.getRestaurantName(), order1.getUserName(), order2.getUserName()),
                Arrays.asList(agent.getName(), order2.getRestaurantName(), order1.getRestaurantName(), order2.getUserName(), order1.getUserName())
        );

        double minTime = Double.POSITIVE_INFINITY;
        List<String> bestPath = null;

        AllPossiblePathEntry trace = new AllPossiblePathEntry();
        List<PathEntry> allPossiblePaths = new ArrayList<>();

        for (List<String> path : permutations) {
            double totalTime = calculateTotalTime(graph, locations, path);

            PathEntry pathEntry = new PathEntry();
            pathEntry.setPath(path);
            pathEntry.setPathCost(totalTime);
            allPossiblePaths.add(pathEntry);

            if (totalTime < minTime) {
                minTime = totalTime;
                bestPath = path;
            }
        }

        PathEntry minCostPath = new PathEntry();
        minCostPath.setPath(bestPath);
        minCostPath.setPathCost(minTime);

        trace.setAllPossiblePath(allPossiblePaths);
        trace.setMinCostPath(minCostPath);

        System.out.println(true);

        // Output the best path and minimum time
        System.out.println("Best path: " + bestPath);
        System.out.println("Minimum time: " + minTime + " hours");
        return trace;
    }

    static double calculateDistance(Location loc1, Location loc2) {
        if (loc1 != null && loc2 != null) {
            //using strategy pattern so that in future if new algo comes we can switch to that easily
            DistanceCalculatorContext distanceCalculatorContext = new DistanceCalculatorContext(new HaversineDistanceCalculatorStrategy());
            return distanceCalculatorContext.calculateDistance(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
        }
        return 0.0;
    }

    static double calculateTotalTime(Map<String, List<Edge>> graph, Map<String, Location> locations, List<String> path) {
        double totalTime = 0.0;

        for (int i = 0; i < path.size() - 1; i++) {
            String currentLocation = path.get(i);
            String nextLocation = path.get(i + 1);

            double distance = 0.0;
            for (Edge edge : graph.get(currentLocation)) {
                if (edge.destination.equals(nextLocation)) {
                    distance = edge.distance;
                    break;
                }
            }

            double travelTime = distance / Constant.Common.AVG_SPEED_OF_DELIVERY_AGENT; // Convert distance to hours at 20 km/hr
            double prepareTime = locations.get(nextLocation).prepareTime;

            // Check if the total travel time from the starting point is less than the prepare time
            if (totalTime + travelTime < prepareTime) {
                totalTime = prepareTime;
            } else {
                totalTime = totalTime + travelTime;
            }
        }

        return totalTime;
    }
}
