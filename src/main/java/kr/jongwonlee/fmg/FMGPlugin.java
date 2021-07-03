package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.conf.DataStore;
import kr.jongwonlee.fmg.conf.ItemStore;
import kr.jongwonlee.fmg.conf.LocationStore;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.game.GameStore;
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
        FMGCommand.init();
        FMGListener.init();
        GameAlert.init();
        EventBundle.init();
        ItemStore.init();
        DataStore.init();
        LocationStore.init();
        Settings.init();
        GameStore.init();
    }

    @Override
    public void onDisable() {

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

    public static void runTaskLater(Runnable task, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay);
    }

}
