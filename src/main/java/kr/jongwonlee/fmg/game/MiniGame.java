package kr.jongwonlee.fmg.game;

import kr.jongwonlee.fmg.proc.EventBundle;
import kr.jongwonlee.fmg.proc.ProcBundle;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.FileParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class MiniGame {

    private final String name;
    private Map<String, ProcBundle> bundleMap;
    private final Map<UUID, GameData> playersData;
    private final GameData gameData;

    public MiniGame(String name) {
        this.name = name;
        bundleMap = FileParser.parseBundles(name);
        playersData = new HashMap<>();
        gameData = new GameData();
    }

    public void run(String name, Player player) {
        if (bundleMap.containsKey(name)) bundleMap.get(name).run(this, player);
    }

    public void run(String name, ProcUnit procUnit) {
        if (bundleMap.containsKey(name)) bundleMap.get(name).run(this, procUnit);
    }

    public void run(EventBundle name) {
        String eventName = name.getName();
        if (bundleMap.containsKey(eventName)) bundleMap.get(eventName).run(this);
    }

    public void run(EventBundle name, Player player) {
        String eventName = name.getName();
        if (bundleMap.containsKey(eventName)) bundleMap.get(eventName).run(this, player);
    }

    public String getName() {
        return name;
    }

    public Map<UUID, GameData> getPlayersData() {
        return playersData;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void join(UUID playerUuid) {
        Player player = toPlayer(playerUuid);
        run(EventBundle.PRE_GAME_JOIN, player);
        if (playersData.containsKey(playerUuid)) return;
        playersData.put(playerUuid, new GameData());
        GameStore.setGame(player, this);
        run(EventBundle.GAME_JOIN, player);
    }

    public void quit(UUID playerUuid) {
        if (!playersData.containsKey(playerUuid)) return;
        Player player = toPlayer(playerUuid);
        run(EventBundle.PRE_GAME_LEFT, player);
        playersData.remove(playerUuid);
        GameStore.removeGame(player);
        run(EventBundle.GAME_LEFT, player);
        if (playersData.isEmpty()) disable();
    }

    public static Player toPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public void reload() {
        bundleMap = FileParser.parseBundles(name);
    }

    public void disable() {
        run(EventBundle.PRE_GAME_STOP);
        playersData.keySet().forEach(this::quit);
        playersData.clear();
        gameData.clear();
        run(EventBundle.GAME_STOP);
    }

}
