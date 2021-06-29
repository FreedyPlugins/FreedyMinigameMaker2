package kr.jongwonlee.fmg.game;

import java.util.HashMap;
import java.util.Map;

public class GameData {

    private final Map<String, String> dataMap;

    public GameData() {
        this.dataMap = new HashMap<>();
    }

    public GameData(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    public String getData(String key) {
        return dataMap.getOrDefault(key, "null");
    }

    public void setData(String key, String value) {
        if (value == null) dataMap.remove(key);
        else dataMap.put(key, value);
    }

    public void clear() {
        dataMap.clear();
    }

}
