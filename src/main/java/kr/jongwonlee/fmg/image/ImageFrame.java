package kr.jongwonlee.fmg.image;


import net.minecraft.world.entity.decoration.EntityItemFrame;

public class ImageFrame {

    private byte[] pixels;
    private EntityItemFrame frame;

    public byte[] getPixels() {
        return pixels;
    }

    public void setPixels(byte[] pixels) {
        this.pixels = pixels;
    }

    public EntityItemFrame getFrame() {
        return frame;
    }

    public void setFrame(EntityItemFrame frame) {
        this.frame = frame;
    }

}
