package kr.jongwonlee.fmg.image;

import kr.jongwonlee.fmg.nms.NMS;
import kr.jongwonlee.fmg.nms.v1_12_R1.ImageFrame;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Image {

    public Player player;
    public BufferedImage bufferedImage;
    public Map<String, Drawable> drawableMap;
    public BlockFace face;
    public Location location;
    public boolean isSettled;
    public ImageFrame[][] subImages;

    public Drawable getDrawable(String name) {
        return drawableMap.get(name);
    }

    private Image(Player player) {
        this.player = player;
        this.location = player.getEyeLocation();
        drawableMap = new HashMap<>();
        this.face = yawToFace(player.getLocation().getYaw(), false);
        this.bufferedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
//        this.imageFile = new ImageFile("default");
//        try {
//            this.bufferedImage = ImageIO.read(imageFile.getFile());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
        setSettled(true);
    }


    public BlockFace getFace() {
        return face;
    }

    public void setFace(BlockFace face) {
        this.face = face;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public void setSettled(boolean settled) {
        isSettled = settled;
    }

    public void update() {
        if (!isSettled()) return;
        setSettled(false);
        this.bufferedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        renderImage();
        NMS.getImageViewer().createMap(this);
        setSettled(true);
        NMS.getImageViewer().applyMap(this);
    }

    public void renderImage() {
        Graphics2D g2d = bufferedImage.createGraphics();
        initialGraphic2D(g2d);
        drawableMap.values().forEach(drawable -> drawable.draw(g2d));
        g2d.dispose();
    }

    public static void initialGraphic2D(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    private static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    private static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };

    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections)
            return radial[Math.round(yaw / 45f) & 0x7];

        return axis[Math.round(yaw / 90f) & 0x3];
    }

    public static Image getNewImage(Player player) {
        return new Image(player);
    }

}