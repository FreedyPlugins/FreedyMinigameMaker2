package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Processable(alias = {"location", "loc"})
public class Location implements Process {

    SmallFrontBrace frontBrace;
    boolean isGame;
    boolean isOnline;
    boolean isSet;
    boolean isAdd;
    boolean isPosX;
    boolean isPosY;
    boolean isPosZ;
    boolean isPosYaw;
    boolean isPosPitch;

    @Override
    public ProcType getType() {
        return ProcType.LOCATION;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isPosX = parseUnit.useExecutor(ProcType.EXECUTE_POS_X);
        isPosY = parseUnit.useExecutor(ProcType.EXECUTE_POS_Y);
        isPosZ = parseUnit.useExecutor(ProcType.EXECUTE_POS_Z);
        isPosYaw = parseUnit.useExecutor(ProcType.EXECUTE_POS_YAW);
        isPosPitch = parseUnit.useExecutor(ProcType.EXECUTE_POS_PITCH);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) {
            parseUnit.addExecutor(getType());
            return;
        } else frontBrace = ((SmallFrontBrace) process);
        frontBrace.cutBehindEndBrace();
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace == null) return "";
            java.util.List<Process> processList = frontBrace.getProcessList();
            String name = processList.get(0).run(miniGame, procUnit);
            Player player = procUnit.target.player;
            if (isOnline) {
                if (isPosX) {
                    org.bukkit.Location location = GameDataStore.getInst().getLocation(name);
                    return String.valueOf(location.getX()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosY) {
                    org.bukkit.Location location = GameDataStore.getInst().getLocation(name);
                    return String.valueOf(location.getY()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosZ) {
                    org.bukkit.Location location = GameDataStore.getInst().getLocation(name);
                    return String.valueOf(location.getZ()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosYaw) {
                    org.bukkit.Location location = GameDataStore.getInst().getLocation(name);
                    return String.valueOf(location.getYaw()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosPitch) {
                    org.bukkit.Location location = GameDataStore.getInst().getLocation(name);
                    return String.valueOf(location.getPitch()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isSet) {
                    World world = Bukkit.getWorld(processList.get(2).run(miniGame, procUnit));
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    boolean hasDirection = processList.size() >= 12;
                    float yaw = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(10).run(miniGame, procUnit));
                    float pitch = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(12).run(miniGame, procUnit));
                    GameDataStore.getInst().setLocation(name, new org.bukkit.Location(world, posX, posY, posZ, yaw, pitch));
                } else if (isAdd) {
                    org.bukkit.Location location = GameDataStore.getInst().getLocation(name);
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    location.add(posX, posY, posZ);
                }
            } else if (isGame) {
                if (isPosX) {
                    org.bukkit.Location location = miniGame.getGameData().getLocation(name);
                    return String.valueOf(location.getX()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosY) {
                    org.bukkit.Location location = miniGame.getGameData().getLocation(name);
                    return String.valueOf(location.getY()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosZ) {
                    org.bukkit.Location location = miniGame.getGameData().getLocation(name);
                    return String.valueOf(location.getZ()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosYaw) {
                    org.bukkit.Location location = miniGame.getGameData().getLocation(name);
                    return String.valueOf(location.getYaw()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosPitch) {
                    org.bukkit.Location location = miniGame.getGameData().getLocation(name);
                    return String.valueOf(location.getPitch()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isSet) {
                    World world = Bukkit.getWorld(processList.get(2).run(miniGame, procUnit));
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    boolean hasDirection = processList.size() >= 12;
                    float yaw = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(10).run(miniGame, procUnit));
                    float pitch = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(12).run(miniGame, procUnit));
                    miniGame.getGameData().setLocation(name, new org.bukkit.Location(world, posX, posY, posZ, yaw, pitch));
                } else if (isAdd) {
                    org.bukkit.Location location = miniGame.getGameData().getLocation(name);
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    location.add(posX, posY, posZ);
                }
            } else if (player != null) {
                if (isPosX) {
                    org.bukkit.Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(name);
                    return String.valueOf(location.getX()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosY) {
                    org.bukkit.Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(name);
                    return String.valueOf(location.getY()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosZ) {
                    org.bukkit.Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(name);
                    return String.valueOf(location.getZ()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosYaw) {
                    org.bukkit.Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(name);
                    return String.valueOf(location.getYaw()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isPosPitch) {
                    org.bukkit.Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(name);
                    return String.valueOf(location.getPitch()) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isSet) {
                    World world = Bukkit.getWorld(processList.get(2).run(miniGame, procUnit));
                    double posX = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(8).run(miniGame, procUnit));
                    boolean hasDirection = processList.size() >= 12;
                    float yaw = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(10).run(miniGame, procUnit));
                    float pitch = !hasDirection ? 0 : (float) Double.parseDouble(processList.get(12).run(miniGame, procUnit));
                    miniGame.getPlayerData(player.getUniqueId()).setLocation(name, new org.bukkit.Location(world, posX, posY, posZ, yaw, pitch));
                } else if (isAdd) {
                    org.bukkit.Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(name);
                    double posX = Double.parseDouble(processList.get(2).run(miniGame, procUnit));
                    double posY = Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                    double posZ = Double.parseDouble(processList.get(6).run(miniGame, procUnit));
                    location.add(posX, posY, posZ);
                }
            }
        } catch (Exception e) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}
