package kr.jongwonlee.fmg.setting;

import kr.jongwonlee.fmg.util.YamlStore;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class LocationStore {

    private static final String ROOT = "locations.";
    private static Map<String, Location> locationMap = new HashMap<>();

    public static void init() {
        YamlStore yamlStore = new YamlStore("locations.yml");
        locationMap = yamlStore.getLocationMap(ROOT);
    }

    public static Location getLocation(String name) {
        return locationMap.getOrDefault(ROOT + name, null);
    }

    public static void setLocation(String name, Location location) {
        YamlStore yamlStore = new YamlStore("locations.yml");
        if (location != null) {
            yamlStore.setLocation(ROOT + name, location);
            yamlStore.save();
            locationMap.put(name, location);
        } else {
            yamlStore.set(ROOT + name, null);
            yamlStore.save();
            locationMap.remove(name);
        }
    }

}
