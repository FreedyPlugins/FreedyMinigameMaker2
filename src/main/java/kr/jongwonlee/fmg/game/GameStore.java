package kr.jongwonlee.fmg.game;

import kr.jongwonlee.fmg.FMGPlugin;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.util.FileStore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class GameStore implements Listener {

    private static Map<String, MiniGame> gameMap;
    private static Map<Player, MiniGame> playerGameMap;
    private static MiniGame hubGame;
    private static GameStore gameStore;

    public static void init() {
        if (gameMap == null) gameMap = new HashMap<>();
        if (playerGameMap == null) playerGameMap = new HashMap<>();
        createGame(Settings.getHubGameName());
        hubGame = getGame(Settings.getHubGameName());
        List<String> dirFiles = FileStore.getDirFiles("");
        dirFiles.forEach(GameStore::loadGame);
        for (String name : gameMap.keySet()) {
            if (!dirFiles.contains(name)) {
                removeShop(name);
            }
        }
        if (GameStore.gameStore == null) {
            GameStore gameStore = new GameStore();
            FMGPlugin.registerEvent(gameStore);
            GameStore.gameStore = gameStore;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        GameStore.getHubGame().join(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        MiniGame game = getGame(player);
        if (game != null) game.quit(player.getUniqueId());
        playerGameMap.remove(player);
    }

    public static MiniGame getGame(Player player) {
        if (player == null || !player.isOnline()) return null;
        return playerGameMap.getOrDefault(player, hubGame);
    }

    public static void setGame(Player player, MiniGame miniGame) {
        playerGameMap.put(player, miniGame);
    }

    public static void removeGame(Player player) {
        playerGameMap.remove(player);
    }

    @Deprecated
    public static void createGame(String name, MiniGame game) {
        if (isGame(name)) return;
        gameMap.put(name, game);
    }

    public static void loadGame(String name) {
        if (isGame(name)) return;
        final MiniGame game = new MiniGame(name);
        gameMap.put(name, game);
    }

    @Deprecated
    public static void unloadGame(String name) {
        if (isGame(name)) return;
        gameMap.remove(name);
    }

    public static MiniGame getGame(String name) {
        return gameMap.getOrDefault(name, null);
    }

    public static void createGame(String name) {
        if (isGame(name)) return;
        loadGame(name);
    }

    public static void removeShop(String name) {
        final MiniGame game = getGame(name);
        if (game == null) return;
        new FileStore(name + Settings.getExtension(), true, false).deleteFile();
        gameMap.remove(name);
    }

    public static MiniGame getHubGame() {
        return hubGame;
    }

    public static boolean isGame(String name) {
        return gameMap.containsKey(name);
    }

    public static Set<String> getGameNames() {
        return gameMap.keySet();
    }

    public static Collection<MiniGame> getGames() {
        return gameMap.values();
    }

}
