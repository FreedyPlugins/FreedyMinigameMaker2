package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Location;
import org.bukkit.Material;
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
    boolean isGet;
    boolean isClone;
    boolean isRemove;

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
        isGet = parseUnit.useExecutor(ProcType.EXECUTE_GET);
        isClone = parseUnit.useExecutor(ProcType.EXECUTE_CLONE);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        if (isGet) {
            parseUnit.addExecutor(getType());
            return;
        }
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
        parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace == null) return "";
            java.util.List<Process> processList = frontBrace.getProcessList();
            String name = processList.get(0).run(miniGame, procUnit);
            Player player = procUnit.target.player;
            if (isGame) {
                if (isRemove) {
                    miniGame.getGameData().setBlock(name, null);
                } else if (isClone) {
                    BlockState block = miniGame.getGameData().getBlock(name);
                    Process process = processList.get(2);
                    String value = process.run(miniGame, procUnit);
                    org.bukkit.block.Block targetBlock;
                    if (process.getType() == ProcType.EXECUTE_GAME) targetBlock = miniGame.getGameData().getBlock(value).getBlock();
                    else targetBlock = GameStore.getPlayerData(player.getUniqueId()).getBlock(value).getBlock();
                    if (targetBlock == null) getGameData(miniGame, procUnit, process.getType())
                            .setBlock(value, block.getBlock().getState());
                    else {
                        targetBlock.setType(block.getType());
                        targetBlock.setData(block.getRawData());
                    }
                } else if (isLocation) {
                    BlockState block = miniGame.getGameData().getBlock(name);
                    Process process = processList.get(2);
                    boolean isGameLocation = process.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllLocation = process.getType() == ProcType.EXECUTE_ONLINE;
                    String targetLocation = process.run(miniGame, procUnit);
                    if (isGameLocation) miniGame.getGameData().setLocation(targetLocation, block.getLocation().clone());
                    else if (isAllLocation) GameDataStore.getInst().setLocation(targetLocation, block.getLocation().clone());
                    else if (player != null) GameStore.getPlayerData(player.getUniqueId()).setLocation(targetLocation, block.getLocation().clone());
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
                    boolean isCode = process.getType() == ProcType.EXECUTE_CODE;
                    String value = process.run(miniGame, procUnit);
                    if (isCode) {
                        int typeId = Integer.parseInt(value);
                        BlockState state = miniGame.getGameData().getBlock(name);
                        state.setTypeId(typeId);
                        org.bukkit.block.Block block = state.getBlock();
                        block.setTypeId(typeId);
                        try {
                            Process proc2 = processList.get(4);
                            String value2 = proc2.run(miniGame, procUnit);
                            int dataId = Integer.parseInt(value2);
                            state.setRawData((byte) dataId);
                            block.setData(((byte) dataId));
                        } catch (Exception ignored) { }
                    } else if (isType) {
                        org.bukkit.block.Block block = miniGame.getGameData().getBlock(name).getBlock();
                        block.setType(Material.getMaterial(value));
                    } else if (isGameLocation) {
                        Location location = miniGame.getGameData().getLocation(value);
                        if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                        miniGame.getGameData().setBlock(name, location.getBlock().getState());
                    } else if (isAllLocation) {
                        Location location = GameDataStore.getInst().getLocation(value);
                        if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                        miniGame.getGameData().setBlock(name, location.getBlock().getState());
                    } else {
                        Location location = GameStore.getPlayerData(player.getUniqueId()).getLocation(value);
                        if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                        miniGame.getGameData().setBlock(name, location.getBlock().getState());
                    }
                }
            } else if (player != null) {
                if (isRemove) {
                    GameStore.getPlayerData(player.getUniqueId()).setBlock(name, null);
                } else if (isClone) {
                    BlockState block = GameStore.getPlayerData(player.getUniqueId()).getBlock(name);
                    Process process = processList.get(2);
                    String value = process.run(miniGame, procUnit);
                    BlockState targetBlock;
                    if (process.getType() == ProcType.EXECUTE_GAME) targetBlock = miniGame.getGameData().getBlock(value);
                    else targetBlock = GameStore.getPlayerData(player.getUniqueId()).getBlock(value);
                    if (targetBlock == null) getGameData(miniGame, procUnit, process.getType())
                            .setBlock(value, block.getBlock().getState());
                    else targetBlock.setData(block.getData());
                } else if (isLocation) {
                    BlockState block = GameStore.getPlayerData(player.getUniqueId()).getBlock(name);
                    Process process = processList.get(2);
                    boolean isGameLocation = process.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllLocation = process.getType() == ProcType.EXECUTE_ONLINE;
                    String targetLocation = process.run(miniGame, procUnit);
                    if (isGameLocation) miniGame.getGameData().setLocation(targetLocation, block.getLocation().clone());
                    else if (isAllLocation) GameDataStore.getInst().setLocation(targetLocation, block.getLocation().clone());
                    else GameStore.getPlayerData(player.getUniqueId()).setLocation(targetLocation, block.getLocation().clone());
                } else if (isCode) {
                    BlockState block = GameStore.getPlayerData(player.getUniqueId()).getBlock(name);
                    return block.getTypeId() + ":" + block.getRawData() + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isType) {
                    BlockState block = GameStore.getPlayerData(player.getUniqueId()).getBlock(name);
                    return block.getType().name() + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isSet) {
                    Process process = processList.get(2);
                    boolean isGameLocation = process.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllLocation = process.getType() == ProcType.EXECUTE_ONLINE;
                    boolean isCode = process.getType() == ProcType.EXECUTE_CODE;
                    String value = process.run(miniGame, procUnit);
                    if (isCode) {
                        int typeId = Integer.parseInt(value);
                        org.bukkit.block.Block block = GameStore.getPlayerData(player.getUniqueId()).getBlock(name).getBlock();
                        block.setTypeId(typeId);
                        try {
                            Process proc2 = processList.get(4);
                            String value2 = proc2.run(miniGame, procUnit);
                            int dataId = Integer.parseInt(value2);
                            block.setData((byte) dataId);
                        } catch (Exception ignored) {
                        }
                    } else if (isType) {
                        org.bukkit.block.Block block = GameStore.getPlayerData(player.getUniqueId()).getBlock(name).getBlock();
                        block.setType(Material.getMaterial(value));
                    } else if (isGameLocation) {
                        Location location = miniGame.getGameData().getLocation(value);
                        if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                        GameStore.getPlayerData(player.getUniqueId()).setBlock(name, location.getBlock().getState());
                    } else if (isAllLocation) {
                        Location location = GameDataStore.getInst().getLocation(value);
                        if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                        GameStore.getPlayerData(player.getUniqueId()).setBlock(name, location.getBlock().getState());
                    } else {
                        Location location = GameStore.getPlayerData(player.getUniqueId()).getLocation(value);
                        if (location == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                        GameStore.getPlayerData(player.getUniqueId()).setBlock(name, location.getBlock().getState());
                    }
                }

            }
            return frontBrace.getLastProc().run(miniGame, procUnit);
        } catch (Exception e) {
            return "";
        }
    }

}
