package kr.jongwonlee.fmg.image.drawable;

import kr.jongwonlee.fmg.image.Drawable;
import kr.jongwonlee.fmg.image.Vector2D;

import java.awt.*;

public class ImageText implements Drawable {

    private String string;
    private Vector2D pos;
    private Font font;

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Vector2D getPosPoint() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        Color beforeColor;
        int maxY = fm.getAscent();
        int currentY = (int) (maxY + pos.getY());
        String[] lines = string.split("\\n");
        for (String line : lines) {
            int beforeX = (int) pos.getX();
            for (String string : ("§0" + line).split("§")) {
                if (string.length() == 0) continue;
                beforeColor = colorCodeMap.get(String.valueOf(string.charAt(0)));
                if (string.length() == 1) continue;
                string = string.substring(1);
                g2d.setColor(beforeColor);
                g2d.drawString(string, beforeX, currentY);
                beforeX += (int) font.getStringBounds(string, g2d.getFontRenderContext()).getMaxX();
            }
            currentY += maxY;
        }
    }

}
