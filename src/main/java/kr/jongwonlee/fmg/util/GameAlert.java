package kr.jongwonlee.fmg.util;

import kr.jongwonlee.fmg.FMGPlugin;

public enum GameAlert {

    DUPLICATED_BUNDLE,
    INVALID_PROCESS_PARSE,
    ONLY_PLAYER,
    DUPLICATED_EVENT,
    ;

    private String message;

    public void print(String[] args) {
        FMGPlugin.getInst().getLogger().warning(args == null ? message : String.format(message, (Object) args));
    }

    public void print() {
        FMGPlugin.getInst().getLogger().warning(message);
    }

    public static void init() {
        YamlStore yamlStore = new YamlStore("alert.yml");
        for (GameAlert gameAlert : GameAlert.values()) {
            String eventString = yamlStore.getString(gameAlert.name());
            if (eventString == null) continue;
            gameAlert.message = eventString.toLowerCase();
        }
    }

}
