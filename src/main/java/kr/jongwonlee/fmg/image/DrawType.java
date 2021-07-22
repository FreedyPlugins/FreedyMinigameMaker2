package kr.jongwonlee.fmg.image;


import kr.jongwonlee.fmg.image.drawable.ImageText;

public enum DrawType {

    TEXT(ImageText.class),
    ;

    private final Class<? extends Drawable> drawClass;

    DrawType(Class<? extends Drawable> drawClass) {
        this.drawClass = drawClass;
    }

    public Class<? extends Drawable> getDrawClass() {
        return drawClass;
    }
}
