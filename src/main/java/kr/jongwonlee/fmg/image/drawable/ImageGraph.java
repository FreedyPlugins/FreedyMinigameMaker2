package kr.jongwonlee.fmg.image.drawable;


import kr.jongwonlee.fmg.image.Drawable;
import kr.jongwonlee.fmg.image.Vector2D;

import java.awt.*;
import java.util.List;

public class ImageGraph implements Drawable {

    private Vector2D posPoint;
    private Vector2D distance;
    private Color color;
    private int stroke;
    private List<Integer> values;

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public Vector2D getPosPoint() {
        return posPoint;
    }

    public void setPosPoint(Vector2D posPoint) {
        this.posPoint = posPoint;
    }

    public Vector2D getDistance() {
        return distance;
    }

    public void setDistance(Vector2D distance) {
        this.distance = distance;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(getStroke()));
        g2d.setColor(color);
        int[] xPoints = new int[values.size()];
        int[] yPoints = new int[values.size()];
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = (int) (posPoint.getX() + distance.getX() * i);
            yPoints[i] = (int) (posPoint.getY() + distance.getY() * i - values.get(i));
        }
        g2d.drawPolyline(xPoints, yPoints, xPoints.length);
    }

}
