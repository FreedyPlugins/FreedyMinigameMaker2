package kr.jongwonlee.fmg.conf;

import kr.jongwonlee.fmg.FMGPlugin;
import kr.jongwonlee.fmg.util.YamlStore;
import org.bukkit.Bukkit;

import java.util.Map;

public class DataStore {

    private static Map<String, String> dataMap;
    private static final YamlStore yamlStore = new YamlStore("data.yml");

    public static YamlStore getYamlStore() {
        return yamlStore;
    }

    public static String getData(String key) {
        return dataMap.getOrDefault(key, "null");
    }

    public static void setData(String key, String value) {
        if (value == null || value.equals("null")) {
            dataMap.remove(key);
            FMGPlugin.runTaskAsync(() -> {
                yamlStore.set(key, null);
            });
        }
        else {
            dataMap.put(key, value);
            yamlStore.set(key, value);
        }

    }

    public static void init() {
        dataMap = yamlStore.getStringMap("");
    }

}
