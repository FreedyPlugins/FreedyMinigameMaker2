package kr.jongwonlee.fmg.game;

import kr.jongwonlee.fmg.proc.EventBundle;
import kr.jongwonlee.fmg.proc.ProcBundle;
import kr.jongwonlee.fmg.proc.ProcUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MiniGame {

    private final String bundle;
    private final String name;
    private Map<String, ProcBundle> bundleMap;
    private final List<UUID> players;
    private final GameData gameData;

    public MiniGame(String name, String bundle) {
        this.name = name;
        this.bundle = bundle;
        bundleMap = GameStore.getBundles(bundle);
        players = new ArrayList<>();
        gameData = new GameData();
    }

    public MiniGame(String name) {
        this(name, name);
    }

    public String run(String name, Player player) {
        if (bundleMap.containsKey(name)) return bundleMap.get(name).run(this, player);
        else return "";
    }

    public String run(String name, ProcUnit procUnit) {
        if (bundleMap.containsKey(name)) return bundleMap.get(name).run(this, procUnit);
        else return "";
    }

    public String run(EventBundle name) {
        String eventName = name.getName();
        if (bundleMap.containsKey(eventName)) return bundleMap.get(eventName).run(this);
        else return "";
    }

    public String run(EventBundle name, Player player) {
        String eventName = name.getName();
        if (bundleMap.containsKey(eventName)) return bundleMap.get(eventName).run(this, player);
        else return "";
    }

    public String run(EventBundle name, Player player, Entity entity) {
        String eventName = name.getName();
        if (bundleMap.containsKey(eventName)) return bundleMap.get(eventName).run(this, player, entity);
        else return "";
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public ProcBundle getProcBundle(String name) {
        return bundleMap.getOrDefault(name, null);
    }

    public String getName() {
        return name;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void join(UUID playerUuid) {
        Player player = toPlayer(playerUuid);
        GameStore.getGame(player).quit(playerUuid);
        String result = run(EventBundle.PRE_GAME_JOIN, player);
        if (result.equals("false")) return;
        if (players.contains(playerUuid)) return;
        players.add(playerUuid);
        GameStore.setGame(player, this);
        run(EventBundle.GAME_JOIN, player);
    }

    public void quit(UUID playerUuid) {
        if (!players.contains(playerUuid)) return;
        Player player = toPlayer(playerUuid);
        run(EventBundle.PRE_GAME_LEFT, player);
        players.remove(playerUuid);
        GameStore.removeGame(player);
        run(EventBundle.GAME_LEFT, player);
        if (players.size() == 0) disable();
    }

    public static Player toPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public void reload() {
        bundleMap = GameStore.getBundles(bundle);
        if (bundleMap == null) {
            players.stream().map(Bukkit::getPlayer).forEach(GameStore::removeGame);
            GameStore.unloadGame(name);
        }
    }

    public void disable() {
        String result = run(EventBundle.PRE_GAME_STOP);
        if (result.equals("false")) return;
        gameData.cancelTaskAll();
        new ArrayList<>(players).forEach(this::quit);
        players.clear();
        gameData.clear();
        run(EventBundle.GAME_STOP);
        if (!name.equals(bundle)) GameStore.unloadGame(name);
    }

}
