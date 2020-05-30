package com.company;

import com.company.entities.Node;

import java.util.*;

public final class Algorithms {

    public static void findPaths(Node start) {

        start.setMarker(0);

        Set<Node> settled = new HashSet<>();
        Set<Node> unsettled = new HashSet<>();

        unsettled.add(start);

        while (!unsettled.isEmpty()) {

            Node current = unsettled.stream().min(Comparator.comparingInt(Node::getMarker)).get();
            unsettled.remove(current);
            for (Map.Entry<Node, Integer> pair :
                    current.getVertices().entrySet()) {
                Node node = pair.getKey();
                Integer weight = pair.getValue();
                if (!settled.contains(node)) {
                    calculateDistance(node, weight, current);
                    unsettled.add(node);
                }
            }
            settled.add(current);
        }
    }

    private static void calculateDistance(Node end, Integer weight, Node start){

        int newMarker = start.getMarker() + weight;
        if (newMarker < end.getMarker()) {
            end.setMarker(newMarker);
            LinkedList<Node> shortestPath = new LinkedList<>(start.getShortestPath());
            shortestPath.add(start);
            end.setShortestPath(shortestPath);
        }
    }

}
