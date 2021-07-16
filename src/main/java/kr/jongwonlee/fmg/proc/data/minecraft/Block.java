package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

@Processable(alias = {"block"})
public class Block implements Process {

    SmallFrontBrace frontBrace;
    boolean isGame;
    boolean isSet;
    boolean isType;
    boolean isCode;
    boolean isLocation;

    @Override
    public ProcType getType() {
        return ProcType.BLOCK;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isType = parseUnit.useExecutor(ProcType.EXECUTE_TYPE);
        isCode = parseUnit.useExecutor(ProcType.EXECUTE_CODE);
        isLocation = parseUnit.useExecutor(ProcType.LOCATION);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
        parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) return "";
        java.util.List<Process> processList = frontBrace.getProcessList();
        String name = processList.get(0).run(miniGame, procUnit);
        Player player = procUnit.target.player;
        if (isGame) {
            if (isLocation) {
                BlockState block = miniGame.getGameData().getBlock(name);
                Process process = processList.get(2);
                boolean isGameLocation = process.getType() == ProcType.EXECUTE_GAME;
                boolean isAllLocation = process.getType() == ProcType.EXECUTE_ONLINE;
                String targetLocation = process.run(miniGame, procUnit);
                if (isGameLocation) miniGame.getGameData().setLocation(targetLocation, block.getLocation());
                else if (isAllLocation) GameDataStore.getInst().setLocation(targetLocation, block.getLocation());
                else if (player != null) miniGame.getPlayerData(player.getUniqueId()).setLocation(targetLocation, block.getLocation());
            } else if (isCode) {
                BlockState block = miniGame.getGameData().getBlock(name);
                return block.getTypeId() + ":" + block.getRawData() + frontBrace.getLastProc().run(miniGame, procUnit);
            } else if (isType) {
                BlockState block = miniGame.getGameData().getBlock(name);
                return block.getType().name() + frontBrace.getLastProc().run(miniGame, procUnit);
            } else if (isSet) {
                Process process = processList.get(2);
                boolean isGameLocation = process.getType() == ProcType.EXECUTE_GAME;
                boolean isAllLocation = process.getType() == ProcType.EXECUTE_ONLINE;
                String targetLocation = process.run(miniGame, procUnit);
                if (isGameLocation) {
                    Location location = miniGame.getGameData().getLocation(targetLocation);
                    if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                    miniGame.getGameData().setBlock(name, location.getBlock().getState());
                } else if (isAllLocation) {
                    Location location = GameDataStore.getInst().getLocation(targetLocation);
                    if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                    miniGame.getGameData().setBlock(name, location.getBlock().getState());
                } else {
                    Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(name);
                    if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                    miniGame.getGameData().setBlock(name, location.getBlock().getState());
                }
            }
        } else if (player != null) {
            if (isLocation) {
                BlockState block = miniGame.getPlayerData(player.getUniqueId()).getBlock(name);
                Process process = processList.get(2);
                boolean isGameLocation = process.getType() == ProcType.EXECUTE_GAME;
                boolean isAllLocation = process.getType() == ProcType.EXECUTE_ONLINE;
                String targetLocation = process.run(miniGame, procUnit);
                if (isGameLocation) miniGame.getGameData().setLocation(targetLocation, block.getLocation());
                else if ( isAllLocation) GameDataStore.getInst().setLocation(targetLocation, block.getLocation());
                else miniGame.getPlayerData(player.getUniqueId()).setLocation(targetLocation, block.getLocation());
            } else if (isCode) {
                BlockState block = miniGame.getPlayerData(player.getUniqueId()).getBlock(name);
                return block.getTypeId() + ":" + block.getRawData() + frontBrace.getLastProc().run(miniGame, procUnit);
            } else if (isType) {
                BlockState block = miniGame.getPlayerData(player.getUniqueId()).getBlock(name);
                return block.getType().name() + frontBrace.getLastProc().run(miniGame, procUnit);
            } else if (isSet) {
                Process process = processList.get(2);
                boolean isGameLocation = process.getType() == ProcType.EXECUTE_GAME;
                boolean isAllLocation = process.getType() == ProcType.EXECUTE_ONLINE;
                String targetLocation = process.run(miniGame, procUnit);
                if (isGameLocation) {
                    Location location = miniGame.getGameData().getLocation(targetLocation);
                    if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                    miniGame.getPlayerData(player.getUniqueId()).setBlock(name, location.getBlock().getState());
                } else if (isAllLocation) {
                    Location location = GameDataStore.getInst().getLocation(targetLocation);
                    if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                    miniGame.getPlayerData(player.getUniqueId()).setBlock(name, location.getBlock().getState());
                } else {
                    Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(targetLocation);
                    if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                    miniGame.getPlayerData(player.getUniqueId()).setBlock(name, location.getBlock().getState());
                }
            }

        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}
