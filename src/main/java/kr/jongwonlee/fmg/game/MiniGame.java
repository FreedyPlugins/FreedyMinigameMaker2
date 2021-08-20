package kr.jongwonlee.fmg.game;

import kr.jongwonlee.fmg.proc.EventBundle;
import kr.jongwonlee.fmg.proc.FileParser;
import kr.jongwonlee.fmg.proc.ProcBundle;
import kr.jongwonlee.fmg.proc.ProcUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public ProcBundle getProcBundle(String name) {
        return bundleMap.getOrDefault(name, null);
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

    public GameData getPlayerData(UUID uuid) {
        return GameStore.getHubGame().playersData.getOrDefault(uuid, gameData);
    }

    public void join(UUID playerUuid) {
        if (this != GameStore.getHubGame()) GameStore.getHubGame().quit(playerUuid);
        Player player = toPlayer(playerUuid);
        String result = run(EventBundle.PRE_GAME_JOIN, player);
        if (result.equals("false")) return;
        if (playersData.containsKey(playerUuid)) return;
        playersData.put(playerUuid, new GameData());
        GameStore.setGame(player, this);
        run(EventBundle.GAME_JOIN, player);
    }

    public void quit(UUID playerUuid) {
        if (!playersData.containsKey(playerUuid)) return;
        Player player = toPlayer(playerUuid);
        getPlayerData(playerUuid).cancelTaskAll();
        run(EventBundle.PRE_GAME_LEFT, player);
        playersData.get(playerUuid).cancelTaskAll();
        playersData.remove(playerUuid);
        GameStore.removeGame(player);
        run(EventBundle.GAME_LEFT, player);
        if (playersData.size() == 0) disable();
        if (this != GameStore.getHubGame()) GameStore.getHubGame().join(playerUuid);
    }

    public static Player toPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public void reload() {
        bundleMap = FileParser.parseBundles(name);
    }

    public void disable() {
        String result = run(EventBundle.PRE_GAME_STOP);
        if (result.equals("false")) return;
        gameData.cancelTaskAll();
        new ArrayList<>(playersData.keySet()).forEach(this::quit);
        playersData.clear();
        gameData.clear();
        run(EventBundle.GAME_STOP);
    }

}
