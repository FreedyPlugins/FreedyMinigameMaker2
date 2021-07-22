package kr.jongwonlee.fmg.nms;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.map.MapPalette;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public interface ImageViewer {

    void createMap(kr.jongwonlee.fmg.image.Image image);

    void applyMap(kr.jongwonlee.fmg.image.Image image);

    void destroyMap(kr.jongwonlee.fmg.image.Image image);

    default Location addLocation(BlockFace face, Location location, int x, int y) {
        Location loc = location.clone();
        switch (face) {
            case EAST:
                loc.add(0, y, x);
                break;
            case WEST:
                loc.add(0, y, -x);
                break;
            case NORTH:
                loc.add(x, y, 0);
                break;
            case SOUTH:
                loc.add(-x, y, 0);
                break;
            default:
                return null;
        }
        return loc;
    }

    default byte[] createPixels(BufferedImage image) {

        int pixelCount = image.getWidth() * image.getHeight();
        int[] pixels = new int[pixelCount];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        byte[] colors = new byte[pixelCount];
        for (int i = 0; i < pixelCount; i++) {
            colors[i] = MapPalette.matchColor(new Color(pixels[i], true));
        }

        return colors;
    }

    default BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        return new BufferedImage(cm, bi.copyData(null), cm.isAlphaPremultiplied(), null);
    }


}
