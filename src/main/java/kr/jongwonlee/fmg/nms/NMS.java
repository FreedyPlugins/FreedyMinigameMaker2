package kr.jongwonlee.fmg.nms;

import org.bukkit.Bukkit;

public class NMS {

    public static void init() {
        String version;
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return;
        }
        switch (version) {
            case "v1_12_R1":
                break;
            case "v1_17_R2":
                break;
        }
    }

}
