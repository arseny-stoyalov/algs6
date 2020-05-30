package com.company.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {

    private final Map<Node, Integer> vertices = new HashMap<>();

    private int marker = Integer.MAX_VALUE;

    private List<Node> shortestPath = new LinkedList<>();

    public void connectTo(Node node, int weight) {
        this.vertices.put(node, weight);
    }

    public void removeConnection(Node node) {
        this.vertices.remove(node);
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Map<Node, Integer> getVertices() {
        return Map.copyOf(vertices);
    }

    public int getMarker() {
        return marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }

}
