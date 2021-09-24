package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.event.AddonDisableEvent;
import kr.jongwonlee.fmg.event.AddonEnableEvent;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.nms.NMS;
import kr.jongwonlee.fmg.proc.EventBundle;
import kr.jongwonlee.fmg.util.GameAlert;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        new MetricsLite(this, 12184);
        NMS.init();
        FMGCommand.init();
        FMGListener.init();
        GameAlert.init();
        EventBundle.init();
        Settings.init();
        GameDataStore.init();
        Bukkit.getPluginManager().callEvent(new AddonEnableEvent());
        GameStore.init();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        GameStore.getGames().forEach(MiniGame::disable);
        GameDataStore.save();
        Bukkit.getPluginManager().callEvent(new AddonDisableEvent());
    }

    public static void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, getInst());
    }

    public static void unregisterEvent(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public static int runTask(Runnable task) {
        return Bukkit.getScheduler().runTask(getInst(), task).getTaskId();
    }

    public static int runTaskAsync(Runnable task) {
        return Bukkit.getScheduler().runTaskAsynchronously(getInst(), task).getTaskId();
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
