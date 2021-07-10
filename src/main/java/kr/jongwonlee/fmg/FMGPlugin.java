package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.conf.DataStore;
import kr.jongwonlee.fmg.conf.ItemStore;
import kr.jongwonlee.fmg.conf.LocationStore;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.game.GameStore;
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
        ItemStore.init();
        DataStore.init();
        LocationStore.init();
        Settings.init();
        GameStore.init();
    }

    @Override
    public void onDisable() {
        DataStore.getYamlStore().save();
        ItemStore.getYamlStore().save();
        LocationStore.getYamlStore().save();
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
