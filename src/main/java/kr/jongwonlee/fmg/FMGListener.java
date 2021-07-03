package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.proc.EventBundle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class FMGListener implements Listener {

    public static void init() {
        FMGPlugin.registerEvent(new FMGListener());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        GameStore.getGame(player).run(EventBundle.MOVE, player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        GameStore.getGame(player).run(EventBundle.INTERACT, player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

    }

}
