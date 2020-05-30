package com.company.gui.drawables;

import com.company.entities.Node;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static java.lang.Math.abs;

public class NodeView implements DrawableItem {

    private final String id;

    private final Point center;

    private static final int DIAMETER = 50;

    private Color color;

    private final Node node = new Node();

    public NodeView(Point center, Color color, String id) {
        this.center = center;
        this.color = color;
        this.id = id;
    }

    public NodeView(int x, int y, Color color, String id) {
        this.center = new Point(x, y);
        this.color = color;
        this.id = id;
    }

    public Node getNode() {
        return node;
    }

    public String getId() {
        return id;
    }

    public void addConnection(NodeView node, int weight) {
        this.node.connectTo(node.node, weight);
    }

    public Point getCenter() {
        return this.center;
    }

    public boolean hitNode(NodeView node) {
        return abs(center.x - node.center.x) < DIAMETER
                && abs(center.y - node.center.y) < DIAMETER;
    }

    public boolean hitWall(Dimension container) {

        int radius = DIAMETER / 2;
        return center.x + radius > container.width
                || center.y + radius > container.height
                || center.x - radius < 0 || center.y - radius < 0;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        int radius = NodeView.DIAMETER / 2;
        Ellipse2D node = new Ellipse2D.Double(center.x - radius, center.y - radius, DIAMETER, DIAMETER);
        ((Graphics2D) g).draw(node);
        g.setColor(color);
        ((Graphics2D) g).fill(node);
        g.setColor(Color.BLACK);
    }

    public boolean contains(Point p) {
        int radius = DIAMETER / 2;
        return abs(center.x - p.x) <= radius
                && abs(center.y - p.y) <= radius;
    }

}
