package kr.jongwonlee.fmg.image.drawable;

import kr.jongwonlee.fmg.image.Drawable;
import kr.jongwonlee.fmg.image.Vector2D;

import java.awt.*;

public class ImageBar implements Drawable {


    private Vector2D posPoint;
    private Vector2D secondPos;
    private Color color;
    private Color baseColor;
    private int process;

    public Vector2D getPosPoint() {
        return posPoint;
    }

    public void setPosPoint(Vector2D posPoint) {
        this.posPoint = posPoint;
    }

    public Vector2D getSecondPos() {
        return secondPos;
    }

    public void setSecondPos(Vector2D secondPos) {
        this.secondPos = secondPos;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor = baseColor;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    @Override
    public void draw(Graphics2D g2d) {
        int shortLine = (int) Math.min(secondPos.getX(), secondPos.getY());
        int longLine = (int) Math.max(secondPos.getX(), secondPos.getY());
        g2d.setColor(baseColor);
        g2d.fillRoundRect(((int) posPoint.getX()), ((int) posPoint.getY()), ((int) secondPos.getX()), ((int) secondPos.getY()), shortLine, shortLine);
        if (process == 0) return;
        int process = ((int) (getProcess() / 100D * ((double) longLine)));
        boolean isXLine = secondPos.getX() > secondPos.getY();
        g2d.setColor(color);
        g2d.fillRoundRect(((int) posPoint.getX()), ((int) posPoint.getY()), isXLine ? process : shortLine, isXLine ? shortLine : process, shortLine, shortLine);
    }

}
