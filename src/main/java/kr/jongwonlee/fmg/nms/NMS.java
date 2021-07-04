package kr.jongwonlee.fmg.nms;

import kr.jongwonlee.fmg.FMGPlugin;
import org.bukkit.Bukkit;

public class NMS {

    private static ImageViewer imageViewer;

    public static ImageViewer getImageViewer() {
        return imageViewer;
    }

    public static void init() {
        String version;
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return;
        }
        switch (version) {
            case "v1_12_R1":
                imageViewer = new kr.jongwonlee.fmg.nms.v1_12_R1.Viewer();
                break;
            case "v1_17_R2":
                imageViewer = new kr.jongwonlee.fmg.nms.v1_17_R1.Viewer();
                break;
        }
    }

}
