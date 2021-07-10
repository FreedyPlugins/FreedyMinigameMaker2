package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.LocationStore;
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

    @Override
    public ProcType getType() {
        return ProcType.BLOCK;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isType = parseUnit.useExecutor(ProcType.EXECUTE_TYPE);
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
            if (isType) {
                BlockState block = miniGame.getGameData().getBlock(name);
                return block.getType().name();
            } else if (isSet) {
                Process process = processList.get(2);
                boolean isGameLocation = process.getType() == ProcType.EXECUTE_GAME;
                boolean isAllLocation = process.getType() == ProcType.EXECUTE_ONLINE;
                String targetLocation = process.run(miniGame, procUnit);
                if (isGameLocation) {
                    Location location = miniGame.getGameData().getLocation(targetLocation);
                    if (location == null) return "";
                    miniGame.getGameData().setBlock(name, location.getBlock().getState());
                } else if (isAllLocation) {
                    Location location = LocationStore.getLocation(targetLocation);
                    if (location == null) return "";
                    miniGame.getGameData().setBlock(name, location.getBlock().getState());
                } else {
                    Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(name);
                    if (location == null) return "";
                    miniGame.getGameData().setBlock(name, location.getBlock().getState());
                }
            }
        } else if (player != null) {
            if (isType) {
                BlockState block = miniGame.getPlayerData(player.getUniqueId()).getBlock(name);
                return block.getType().name();
            } else if (isSet) {
                Process process = processList.get(2);
                boolean isGameLocation = process.getType() == ProcType.EXECUTE_GAME;
                boolean isAllLocation = process.getType() == ProcType.EXECUTE_ONLINE;
                String targetLocation = process.run(miniGame, procUnit);
                if (isGameLocation) {
                    Location location = miniGame.getGameData().getLocation(targetLocation);
                    if (location == null) return "";
                    miniGame.getPlayerData(player.getUniqueId()).setBlock(name, location.getBlock().getState());
                } else if (isAllLocation) {
                    Location location = LocationStore.getLocation(targetLocation);
                    if (location == null) return "";
                    miniGame.getPlayerData(player.getUniqueId()).setBlock(name, location.getBlock().getState());
                } else {
                    Location location = miniGame.getPlayerData(player.getUniqueId()).getLocation(targetLocation);
                    if (location == null) return "";
                    miniGame.getPlayerData(player.getUniqueId()).setBlock(name, location.getBlock().getState());
                }
            }

        }
        return "";
    }

}
