package kr.jongwonlee.fmg.game;

import kr.jongwonlee.fmg.FMGPlugin;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.proc.FileParser;
import kr.jongwonlee.fmg.proc.ProcBundle;
import kr.jongwonlee.fmg.util.FileStore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.*;

public class GameStore implements Listener {

    private static Map<String, Map<String, ProcBundle>> bundleMap;
    private static Map<String, MiniGame> gameMap;
    private static Map<Player, MiniGame> playerGameMap;
    private static MiniGame hubGame;
    private static GameStore gameStore;
    private static Map<UUID, GameData> playersData = new HashMap<>();

    public static void init() {
        if (gameStore == null) {
            gameStore = new GameStore();
            FMGPlugin.registerEvent(gameStore);
        }
        if (gameMap == null) gameMap = new HashMap<>();
        if (playerGameMap == null) playerGameMap = new HashMap<>();
        bundleMap = new HashMap<>();
        new FileStore(FileStore.DIR + File.separator + Settings.getHubGameName() + Settings.getExtension()).create(true);
        List<String> dirFiles = FileStore.getDirFiles("");
        dirFiles.forEach(name -> bundleMap.put(name, FileParser.parseBundles(name)));
        createGame(Settings.getHubGameName());
        hubGame = getGame(Settings.getHubGameName());
        new ArrayList<>(bundleMap.keySet()).forEach(GameStore::createGame);
        new ArrayList<>(gameMap.values()).forEach(MiniGame::reload);
    }

    public static Map<String, ProcBundle> getBundles(String name) {
        return bundleMap.getOrDefault(name, null);
    }

    public static Map<UUID, GameData> getPlayersData() {
        return playersData;
    }

    public static GameData getPlayerData(UUID uuid) {
        return playersData.getOrDefault(uuid, null);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID uniqueId = player.getUniqueId();
        playersData.put(uniqueId, new GameData());
        getHubGame().join(uniqueId);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final MiniGame game = getGame(player);
        final UUID uniqueId = player.getUniqueId();
        if (game != null) game.quit(uniqueId);
        getHubGame().quit(uniqueId);
        playerGameMap.remove(player);
        playersData.remove(uniqueId);
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

    public static void createGame(String name, MiniGame game) {
        if (isGame(name)) return;
        gameMap.put(name, game);
    }

    public static void loadGame(String name) {
        if (isGame(name)) return;
        final MiniGame game = new MiniGame(name);
        gameMap.put(name, game);
    }

    public static void unloadGame(String name) {
        gameMap.remove(name);
    }

    public static MiniGame getGame(String name) {
        return gameMap.getOrDefault(name, null);
    }

    public static void createGame(String name) {
        if (isGame(name)) return;
        loadGame(name);
    }

    public static void removeGame(String name) {
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
