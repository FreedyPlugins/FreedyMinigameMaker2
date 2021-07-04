package kr.jongwonlee.fmg.image;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public interface Drawable {

    String PATH = "이미지.";



    Map<String, Color> colorCodeMap = new HashMap<String, Color>(){{
        put("0", Color.decode("#000000"));
        put("1", Color.decode("#000080"));
        put("2", Color.decode("#008000"));
        put("3", Color.decode("#008080"));
        put("4", Color.decode("#800000"));
        put("5", Color.decode("#800080"));
        put("6", Color.decode("#808000"));
        put("7", Color.decode("#C0C0C0"));
        put("8", Color.decode("#808080"));
        put("9", Color.decode("#00FFFF"));
        put("a", Color.decode("#00FF00"));
        put("b", Color.decode("#00FFFF"));
        put("c", Color.decode("#FF0000"));
        put("d", Color.decode("#FF00FF"));
        put("e", Color.decode("#FFFF00"));
        put("f", Color.decode("#FFFFFF"));
    }};

    void draw(Graphics2D graphics2D);

}
