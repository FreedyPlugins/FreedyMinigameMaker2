package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.EventBundle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class FMGListener implements Listener {

    public static void init() {
        FMGPlugin.registerEvent(new FMGListener());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setLocation("moveFrom", event.getFrom().clone());
        playerData.setLocation("moveTo", event.getTo().clone());
        String result = game.run(EventBundle.MOVE, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String result = GameStore.getGame(player).run(EventBundle.INTERACT, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

    }

}
