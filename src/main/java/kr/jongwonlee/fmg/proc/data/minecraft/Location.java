package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

@Processable(alias = {"location", "loc"})
public class Location implements Process {

    SmallFrontBrace frontBrace;
    Process process;
    List<Process> processList;
    boolean isGame;
    boolean isOnline;
    boolean isClone;
    boolean isAdd;
    boolean isSet;
    boolean isPosX;
    boolean isPosY;
    boolean isPosZ;
    boolean isPosYaw;
    boolean isPosPitch;
    boolean isRemove;
    boolean isCreate;
    boolean isExists;
    boolean isContains;
    boolean isGet;

    @Override
    public ProcType getType() {
        return ProcType.LOCATION;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        isClone = parseUnit.useExecutor(ProcType.EXECUTE_CLONE);
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isPosX = parseUnit.useExecutor(ProcType.EXECUTE_POS_X);
        isPosY = parseUnit.useExecutor(ProcType.EXECUTE_POS_Y);
        isPosZ = parseUnit.useExecutor(ProcType.EXECUTE_POS_Z);
        isPosYaw = parseUnit.useExecutor(ProcType.EXECUTE_POS_YAW);
        isPosPitch = parseUnit.useExecutor(ProcType.EXECUTE_POS_PITCH);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        isCreate = parseUnit.useExecutor(ProcType.EXECUTE_CREATE);
        isExists = parseUnit.useExecutor(ProcType.EXECUTE_EXISTS);
        isContains = parseUnit.useExecutor(ProcType.EXECUTE_CONTAINS);
        isGet = parseUnit.useExecutor(ProcType.EXECUTE_GET);
        if (isGet) parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
            processList = frontBrace.cutBehindEndBrace();
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace == null) return process.run(miniGame, procUnit);
            Process process = processList.get(0);
            String name = process.run(miniGame, procUnit);
            Player player = procUnit.target.player;
            if (isOnline) {
                org.bukkit.Location location = process.getType() == ProcType.EXECUTE_PLAYER ? player.getLocation() : GameDataStore.getInst().getLocation(name);
                if (isExists) return location == null ? "false" : "true";
                else if (isSet) {
                    double value = Double.parseDouble(processList.get(2).run(miniGame, procUnit));
                    if (isPosX) location.setX(value);
                    else if (isPosY) location.setY(value);
                    else if (isPosZ)  location.setZ(value);
                    else if (isPosYaw)  location.setYaw((float) value);
                    else if (isPosPitch)  location.setPitch((float) value);
                }
                else if (isRemove) GameDataStore.getInst().setLocation(name, null);
                else if (isPosX) return location.getX() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosY) return location.getY() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosZ) return location.getZ() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosYaw) return location.getYaw() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosPitch) return location.getPitch() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isCreate) {
                    World world = Bukkit.getWorld(processList.get(2).run(miniGame, procUnit));
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    boolean hasDirection = processList.size() >= 12;
                    float yaw = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(10).run(miniGame, procUnit));
                    float pitch = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(12).run(miniGame, procUnit));
                    GameDataStore.getInst().setLocation(name, new org.bukkit.Location(world, posX, posY, posZ, yaw, pitch));
                } else if (isClone) {
                    Process proc = processList.get(2);
                    String value = proc.run(miniGame, procUnit);
                    if (proc.getType() == ProcType.EXECUTE_GAME) miniGame.getGameData().setLocation(value, location.clone());
                    else if (proc.getType() == ProcType.EXECUTE_ONLINE) GameDataStore.getInst().setLocation(value, location.clone());
                    else if (player != null) miniGame.getPlayerData(player.getUniqueId()).setLocation(value, location.clone());
                } else if (isAdd) {
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    location.add(posX, posY, posZ);
                } else if (isContains) {
                    Process proc1 = processList.get(2);
                    String value = proc1.run(miniGame, procUnit);
                    org.bukkit.Location pos1;
                    if (proc1.getType() == ProcType.EXECUTE_GAME) pos1 = miniGame.getGameData().getLocation(value);
                    else if (proc1.getType() == ProcType.EXECUTE_ONLINE) pos1 = GameDataStore.getInst().getLocation(value);
                    else pos1 = miniGame.getPlayerData(player.getUniqueId()).getLocation(value);
                    Process proc2 = processList.get(4);
                    String value2 = proc2.run(miniGame, procUnit);
                    org.bukkit.Location pos2;
                    if (proc2.getType() == ProcType.EXECUTE_GAME) pos2 = miniGame.getGameData().getLocation(value2);
                    else if (proc2.getType() == ProcType.EXECUTE_ONLINE) pos2 = GameDataStore.getInst().getLocation(value2);
                    else pos2 = miniGame.getPlayerData(player.getUniqueId()).getLocation(value2);
                    return location.toVector().isInAABB(pos1.toVector(), pos2.toVector()) ? "true" : "false";
                }
            } else if (isGame) {
                org.bukkit.Location location = process.getType() == ProcType.EXECUTE_PLAYER ? player.getLocation() : miniGame.getGameData().getLocation(name);
                if (isExists) return location == null ? "false" : "true";
                else if (isSet) {
                    double value = Double.parseDouble(processList.get(2).run(miniGame, procUnit));
                    if (isPosX) location.setX(value);
                    else if (isPosY) location.setY(value);
                    else if (isPosZ)  location.setZ(value);
                    else if (isPosYaw)  location.setYaw((float) value);
                    else if (isPosPitch)  location.setPitch((float) value);
                }
                else if (isRemove) miniGame.getGameData().setLocation(name, null);
                else if (isPosX) return location.getX() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosY) return location.getY() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosZ) return location.getZ() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosYaw) return location.getYaw() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosPitch) return location.getPitch() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isCreate) {
                    World world = Bukkit.getWorld(processList.get(2).run(miniGame, procUnit));
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    boolean hasDirection = processList.size() >= 12;
                    float yaw = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(10).run(miniGame, procUnit));
                    float pitch = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(12).run(miniGame, procUnit));
                    miniGame.getGameData().setLocation(name, new org.bukkit.Location(world, posX, posY, posZ, yaw, pitch));
                } else if (isClone) {
                    Process proc = processList.get(2);
                    String value = proc.run(miniGame, procUnit);
                    if (proc.getType() == ProcType.EXECUTE_GAME) miniGame.getGameData().setLocation(value, location.clone());
                    else if (proc.getType() == ProcType.EXECUTE_ONLINE) GameDataStore.getInst().setLocation(value, location.clone());
                    else if (player != null) miniGame.getPlayerData(player.getUniqueId()).setLocation(value, location.clone());
                } else if (isAdd) {
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    location.add(posX, posY, posZ);
                } else if (isContains) {
                    Process proc1 = processList.get(2);
                    String value = proc1.run(miniGame, procUnit);
                    org.bukkit.Location pos1;
                    if (proc1.getType() == ProcType.EXECUTE_GAME) pos1 = miniGame.getGameData().getLocation(value);
                    else if (proc1.getType() == ProcType.EXECUTE_ONLINE) pos1 = GameDataStore.getInst().getLocation(value);
                    else pos1 = miniGame.getPlayerData(player.getUniqueId()).getLocation(value);
                    Process proc2 = processList.get(4);
                    String value2 = proc2.run(miniGame, procUnit);
                    org.bukkit.Location pos2;
                    if (proc2.getType() == ProcType.EXECUTE_GAME) pos2 = miniGame.getGameData().getLocation(value2);
                    else if (proc2.getType() == ProcType.EXECUTE_ONLINE) pos2 = GameDataStore.getInst().getLocation(value2);
                    else pos2 = miniGame.getPlayerData(player.getUniqueId()).getLocation(value2);
                    return location.toVector().isInAABB(pos1.toVector(), pos2.toVector()) ? "true" : "false";
                }
            } else if (player != null) {
                org.bukkit.Location location = process.getType() == ProcType.EXECUTE_PLAYER ? player.getLocation() : miniGame.getPlayerData(player.getUniqueId()).getLocation(name);
                if (isExists) return location == null ? "false" : "true";
                else if (isSet) {
                    double value = Double.parseDouble(processList.get(2).run(miniGame, procUnit));
                    if (isPosX) location.setX(value);
                    else if (isPosY) location.setY(value);
                    else if (isPosZ)  location.setZ(value);
                    else if (isPosYaw)  location.setYaw((float) value);
                    else if (isPosPitch)  location.setPitch((float) value);
                }
                else if (isRemove) miniGame.getPlayerData(player.getUniqueId()).setLocation(name, null);
                else if (isPosX) return location.getX() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosY) return location.getY() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosZ) return location.getZ() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosYaw) return location.getYaw() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isPosPitch) return location.getPitch() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isCreate) {
                    World world = Bukkit.getWorld(processList.get(2).run(miniGame, procUnit));
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    boolean hasDirection = processList.size() >= 12;
                    float yaw = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(10).run(miniGame, procUnit));
                    float pitch = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(12).run(miniGame, procUnit));
                    miniGame.getPlayerData(player.getUniqueId()).setLocation(name, new org.bukkit.Location(world, posX, posY, posZ, yaw, pitch));
                } else if (isClone) {
                    Process proc = processList.get(2);
                    String value = proc.run(miniGame, procUnit);
                    if (proc.getType() == ProcType.EXECUTE_GAME) miniGame.getGameData().setLocation(value, location.clone());
                    else if (proc.getType() == ProcType.EXECUTE_ONLINE) GameDataStore.getInst().setLocation(value, location.clone());
                    else miniGame.getPlayerData(player.getUniqueId()).setLocation(value, location.clone());
                } else if (isAdd) {
                    double posX = Double.parseDouble(processList.get(2).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    location.add(posX, posY, posZ);
                } else if (isContains) {
                    Process proc1 = processList.get(2);
                    String value = proc1.run(miniGame, procUnit);
                    org.bukkit.Location pos1;
                    if (proc1.getType() == ProcType.EXECUTE_GAME) pos1 = miniGame.getGameData().getLocation(value);
                    else if (proc1.getType() == ProcType.EXECUTE_ONLINE) pos1 = GameDataStore.getInst().getLocation(value);
                    else pos1 = miniGame.getPlayerData(player.getUniqueId()).getLocation(value);
                    Process proc2 = processList.get(4);
                    String value2 = proc2.run(miniGame, procUnit);
                    org.bukkit.Location pos2;
                    if (proc2.getType() == ProcType.EXECUTE_GAME) pos2 = miniGame.getGameData().getLocation(value2);
                    else if (proc2.getType() == ProcType.EXECUTE_ONLINE) pos2 = GameDataStore.getInst().getLocation(value2);
                    else pos2 = miniGame.getPlayerData(player.getUniqueId()).getLocation(value2);
                    Vector vector1 = pos1.toVector();
                    Vector vector2 = pos2.toVector();
                    Vector max = Vector.getMaximum(vector1, vector2);
                    Vector min = Vector.getMinimum(vector1, vector2);
                    return location.toVector().isInAABB(min, max) ? "true" : "false";
                }
            }
        } catch (Exception e) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}
