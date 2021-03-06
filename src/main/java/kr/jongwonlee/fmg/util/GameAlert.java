package kr.jongwonlee.fmg.util;

import kr.jongwonlee.fmg.FMGPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum GameAlert {

    DUPLICATED_BUNDLE,
    INVALID_PROCESS_PARSE,
    ONLY_PLAYER,
    DUPLICATED_EVENT,
    ERROR,
    EDITOR_SAVE,
    NEED_PERMISSION,
    RELOADING,
    RELOADED,
    SAVING,
    SAVED,
    CREATED,
    DELETED,
    SETTLED,
    INVALID_GAME_NAME,
    GAME_NOT_EXISTS,
    ;

    private String message;

    public void print(String[] args) {
        if (message != null) FMGPlugin.getInst().getLogger().warning(args == null ? message : String.format(message, (Object[]) args));
    }

    public void print(Player player, String[] args) {
        if (player == null) return;
        if (message != null) player.sendMessage(args == null ? message : String.format(message, (Object[]) args));
    }

    public void print(CommandSender sender, String[] args) {
        if (sender == null) return;
        if (message != null) sender.sendMessage(args == null ? message : String.format(message, (Object[]) args));
    }

    public void print() {
        if (message != null) FMGPlugin.getInst().getLogger().warning(message);
    }

    public void printLog() {
        if (message != null) FMGPlugin.getInst().getLogger().info(message);
    }

    public void printLog(String[] args) {
        if (message != null) FMGPlugin.getInst().getLogger().info(args == null ? message : String.format(message, (Object[]) args));
    }

    public static void init() {
        YamlStore yamlStore = new YamlStore("alert.yml");
        for (GameAlert gameAlert : GameAlert.values()) {
            String eventString = yamlStore.getString(gameAlert.name());
            if (eventString == null) continue;
            gameAlert.message = eventString;
        }
    }

}
