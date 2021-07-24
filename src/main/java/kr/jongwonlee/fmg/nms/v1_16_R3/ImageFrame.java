package kr.jongwonlee.fmg.nms.v1_16_R3;

import net.minecraft.server.v1_16_R3.EntityItemFrame;

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
