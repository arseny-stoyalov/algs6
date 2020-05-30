package com.company.gui.drawables;

import java.awt.*;
import java.util.Objects;

import static java.lang.Math.*;

public class EdgeView implements DrawableItem {

    private final NodeView start;

    private final NodeView end;

    private final int weight;

    public EdgeView(NodeView start, NodeView end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
        start.addConnection(end, weight);
    }

    public void removeConnection() {
        start.getNode().removeConnection(end.getNode());
    }

    public int getWeight() {
        return weight;
    }

    public boolean isReversed(EdgeView edge) {
        return equals(edge) || (start.equals(edge.end)
                && end.equals(edge.start));
    }

    @Override
    public void draw(Graphics g) {

        int d = 10;
        int h = 5;
        int dx = end.getCenter().x - start.getCenter().x,
                dy = end.getCenter().y - start.getCenter().y;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + start.getCenter().x;
        ym = xm * sin + ym * cos + start.getCenter().y;
        xm = x;

        x = xn * cos - yn * sin + start.getCenter().x;
        yn = xn * sin + yn * cos + start.getCenter().y;
        xn = x;

        int[] xPoints = {end.getCenter().x, (int) xm, (int) xn};
        int[] yPoints = {end.getCenter().y, (int) ym, (int) yn};

        g.drawLine(start.getCenter().x, start.getCenter().y, end.getCenter().x, end.getCenter().y);
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Color.WHITE);
        g.drawPolygon(xPoints, yPoints, 3);
        g.setColor(Color.BLACK);
        int weightX = abs(dx) / 2;
        weightX += min(start.getCenter().x, end.getCenter().x) + 5;
        int weightY = abs(dy) / 2;
        weightY += min(start.getCenter().y, end.getCenter().y) - 5;
        g.drawString(String.valueOf(weight), weightX, weightY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeView edgeView = (EdgeView) o;
        return start.equals(edgeView.start) &&
                end.equals(edgeView.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
