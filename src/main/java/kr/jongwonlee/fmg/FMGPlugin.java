package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.image.ImageEditor;
import kr.jongwonlee.fmg.nms.NMS;
import kr.jongwonlee.fmg.proc.EventBundle;
import kr.jongwonlee.fmg.util.GameAlert;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class FMGPlugin extends JavaPlugin {

    private static FMGPlugin plugin;

    public static FMGPlugin getInst() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        NMS.init();
        ImageEditor.init();
        FMGCommand.init();
        FMGListener.init();
        GameAlert.init();
        EventBundle.init();
        Settings.init();
        GameDataStore.init();
        GameStore.init();
    }

    @Override
    public void onDisable() {
        GameStore.getGames().forEach(MiniGame::disable);
        GameDataStore.save();
    }

    public static void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, getInst());
    }

    public static void unregisterEvent(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public static void runTask(Runnable task) {
        Bukkit.getScheduler().runTask(getInst(), task);
    }

    public static void runTaskAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(getInst(), task);
    }

    public static int runTaskLater(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLater(plugin, task, delay).getTaskId();
    }

    public static int runTaskLaterAsync(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay).getTaskId();
    }

    public static int runTaskRepeatSync(Runnable task, long delay, long period) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delay, period);
    }

}
