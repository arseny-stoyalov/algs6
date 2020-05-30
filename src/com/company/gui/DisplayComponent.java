package com.company.gui;

import com.company.Algorithms;
import com.company.gui.drawables.DrawableItem;
import com.company.gui.drawables.EdgeView;
import com.company.gui.drawables.NodeView;
import com.company.entities.Node;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayComponent extends JComponent {

    private final Stack<DrawableItem> drawables = new Stack<>();

    private final Set<NodeView> selected = new HashSet<>();

    private NodeView start;

    public final Color DEFAULT_NODE_COLOR = new Color(255, 255, 255, 0);

    public final Color SELECTED_NODE_COLOR = Color.RED;

    public final Color PATH_NODE_COLOR = Color.GREEN;

    private boolean computed;

    private boolean cached;

    private final Map<String, Node> graph = new HashMap<>();

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    protected void paintComponent(Graphics g) {

        for (DrawableItem item : drawables) {
            item.draw(g);
        }
        super.paintComponent(g);
    }

    public void addEdge(Point start, Point end, int weight) {

        Optional<DrawableItem> sNode = drawables.stream().filter(d -> d instanceof NodeView)
                .filter(node -> ((NodeView) node).contains(start)).findFirst();
        Optional<DrawableItem> eNode = drawables.stream().filter(d -> d instanceof NodeView)
                .filter(node -> ((NodeView) node).contains(end)).findFirst();
        if (sNode.isPresent() && eNode.isPresent()) {
            EdgeView edge = new EdgeView((NodeView) sNode.get(), (NodeView) eNode.get(), weight);
            if (drawables.contains(edge))
                return;
            Optional<DrawableItem> e =
                    drawables.stream().filter(d -> d instanceof EdgeView)
                            .filter(edg -> ((EdgeView) edg).isReversed(edge)).findFirst();
            EdgeView ptr = edge;
            if (e.isPresent())
                ptr = new EdgeView((NodeView) sNode.get(), (NodeView) eNode.get()
                        , ((EdgeView) e.get()).getWeight());
            drawables.push(ptr);
            ptr.draw(getGraphics());
        }
    }

    public void addNode(Point center, Color color) {

        NodeView node = new NodeView(center, color, UUID.randomUUID().toString());
        if (node.hitWall(getSize()) ||
                drawables.stream().filter(d -> d instanceof NodeView)
                        .anyMatch(n -> ((NodeView) n).hitNode(node)))
            return;
        drawables.push(node);
        graph.put(node.getId(), node.getNode());
        update();
        node.draw(getGraphics());
    }

    public String selectNode(NodeView node) {

        if (start == node) {
            start = null;
            selected.remove(node);
            node.setColor(DEFAULT_NODE_COLOR);
            cached = false;
            update();
            return null;
        }
        if (computed) update();
        node.setColor(SELECTED_NODE_COLOR);
        selected.add(node);
        if (start != null) {
            List<NodeView> pathParts = compute(start.getNode(), node);
            pathParts.forEach(part -> part.setColor(PATH_NODE_COLOR));
            node.setColor(SELECTED_NODE_COLOR);
            start.setColor(SELECTED_NODE_COLOR);
            selected.addAll(pathParts);
            computed = true;
            return String.valueOf(graph.get(node.getId()).getMarker());
        }
        start = node;
        return null;
    }

    private List<NodeView> compute(Node start, NodeView end) {

        if (!cached) {
            long timeStart = System.nanoTime();
            Algorithms.findPaths(start);
            long timeEnd = System.nanoTime();
            System.out.println("Time taken: " + (timeEnd - timeStart) + "ns");
            cached = true;
        }
        return drawables.stream()
                .filter(d -> d instanceof NodeView)
                .map(n -> (NodeView) n)
                .filter(n -> graph.get(end.getId()).getShortestPath().contains(n.getNode()))
                .collect(Collectors.toList());
    }

    public void removeLastDrawn() {

        if (!drawables.isEmpty()) {
            DrawableItem deleted = drawables.pop();
            if (deleted instanceof EdgeView)
                ((EdgeView) deleted).removeConnection();
            if (deleted instanceof NodeView) {
                graph.remove(((NodeView) deleted).getId());
                start = null;
            }
            update();
        }
    }

    private void update() {

        selected.forEach(n -> n.setColor(DEFAULT_NODE_COLOR));
        selected.clear();
        computed = false;
        if (start == null)
            graph.values().forEach(node -> {
                node.setMarker(Integer.MAX_VALUE);
                node.setShortestPath(new LinkedList<>());
            });
    }

    public Optional<NodeView> occupiedBy(Point point) {

        return drawables.stream()
                .filter(d -> d instanceof NodeView)
                .map(n -> (NodeView) n)
                .filter(n -> n.contains(point))
                .findFirst();
    }

}
