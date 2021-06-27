package kr.jongwonlee.fmg.setting;

import kr.jongwonlee.fmg.GameData;
import kr.jongwonlee.fmg.util.YamlStore;

public class DataStore {

    private static GameData dataMap;

    public static void init() {
        YamlStore yamlStore = new YamlStore("data.yml");
        dataMap = new GameData(yamlStore.getStringMap(""));
    }

    public static String getData(String key) {
        return dataMap.getData(key);
    }

}
