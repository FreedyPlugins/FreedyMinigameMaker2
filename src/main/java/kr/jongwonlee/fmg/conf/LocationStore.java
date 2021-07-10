package kr.jongwonlee.fmg.conf;

import kr.jongwonlee.fmg.FMGPlugin;
import kr.jongwonlee.fmg.util.YamlStore;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class LocationStore {

    private static Map<String, Location> locationMap = new HashMap<>();
    private static final YamlStore yamlStore = new YamlStore("locations.yml");

    public static YamlStore getYamlStore() {
        return yamlStore;
    }

    public static void init() {
        locationMap = yamlStore.getLocationMap("");
    }

    public static Location getLocation(String name) {
        return locationMap.getOrDefault("" + name, null);
    }

    public static void setLocation(String name, Location location) {
        if (location != null) {
            locationMap.put(name, location);
            FMGPlugin.runTaskAsync(() -> {
                yamlStore.setLocation("" + name, location);
            });
        } else {
            locationMap.remove(name);
            FMGPlugin.runTaskAsync(() -> {
                yamlStore.set("" + name, null);
            });
        }
    }

}
