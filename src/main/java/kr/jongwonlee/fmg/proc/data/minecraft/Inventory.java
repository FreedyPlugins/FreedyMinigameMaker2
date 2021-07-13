package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.ItemStore;
import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

@Processable(alias = {"inventory", "inven", "inv"})
public class Inventory implements Process {

    private SmallFrontBrace frontBrace;
    private boolean isAdd;
    private boolean isSet;
    private boolean isCreate;
    private boolean isOpen;
    private boolean isSize;
    private boolean isType;
    private boolean isGame;
    private boolean isEquals;
    private boolean isExists;

    @Override
    public ProcType getType() {
        return ProcType.INVENTORY;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isCreate = parseUnit.useExecutor(ProcType.EXECUTE_CREATE);
        isOpen = parseUnit.useExecutor(ProcType.EXECUTE_OPEN);
        isSize = parseUnit.useExecutor(ProcType.EXECUTE_SIZE);
        isType = parseUnit.useExecutor(ProcType.EXECUTE_TYPE);
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isEquals = parseUnit.useExecutor(ProcType.EXECUTE_EQUALS);
        isExists = parseUnit.useExecutor(ProcType.EXECUTE_EXISTS);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) {
            parseUnit.addExecutor(getType());
            return;
        }
        frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        Player player = procUnit.target.player;
        List<Process> processList = frontBrace.getProcessList();
        Process process = processList.get(0);
        String name = process.run(miniGame, procUnit);
        try {
            if (isGame) {
                org.bukkit.inventory.Inventory inventory = miniGame.getGameData().getInventory(name);
                if (isExists) {
                    return inventory == null ? "false" : "true";
                } else if (isEquals) {
                    Process proc2 = processList.get(2);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameInventory = proc2.getType() == ProcType.EXECUTE_GAME;
                    if (isGameInventory){
                        GameData gameData = miniGame.getGameData();
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false";
                    } else if (player != null) {
                        GameData gameData = miniGame.getPlayerData(player.getUniqueId());
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false";
                    }

                } else if (isAdd) {
                    Process proc = processList.get(2);
                    String value = proc.run(miniGame, procUnit);
                    boolean isGameItemStack = proc.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack){
                        GameData gameData = miniGame.getGameData();
                        ItemStack itemStack = gameData.getItemStack(value);
                        inventory.addItem(itemStack);
                    } else if (isAllItemStack) {
                        ItemStack itemStack = ItemStore.getItemStack(value);
                        inventory.addItem(itemStack);
                    } else if (player != null) {
                        ItemStack itemStack = miniGame.getGameData().getItemStack(value);
                        inventory.addItem(itemStack);
                    }
                } else if (isSet) {
                        String proc = processList.get(2).run(miniGame, procUnit);
                        int index = Integer.parseInt(proc);
                        Process proc2 = processList.get(4);
                        String value = proc2.run(miniGame, procUnit);
                        boolean isGameItemStack = proc2.getType() == ProcType.EXECUTE_GAME;
                        boolean isAllItemStack = proc2.getType() == ProcType.EXECUTE_ONLINE;
                        if (isGameItemStack){
                            GameData gameData = miniGame.getGameData();
                            ItemStack itemStack = gameData.getItemStack(value);
                            inventory.setItem(index, itemStack);
                        } else if (isAllItemStack) {
                            ItemStack itemStack = ItemStore.getItemStack(value);
                            inventory.setItem(index, itemStack);
                        } else if (player != null) {
                            ItemStack itemStack = miniGame.getPlayerData(player.getUniqueId()).getItemStack(value);
                            inventory.setItem(index, itemStack);
                        }
                } else if (isCreate) {
                    String proc = processList.get(2).run(miniGame, procUnit);
                    try {
                        int index = Integer.parseInt(proc);
                        String title = processList.get(4).run(miniGame, procUnit);
                        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, index, title);
                        miniGame.getGameData().setInventory(name, inv);
                    } catch (Exception e) {
                        String title = processList.get(4).run(miniGame, procUnit);
                        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, InventoryType.valueOf(proc), title);
                        miniGame.getGameData().setInventory(name, inv);
                    }
                } else if (isOpen) {
                    if (player != null) player.openInventory(miniGame.getGameData().getInventory(name));
                }
            } else if (player != null) {
                org.bukkit.inventory.Inventory inventory = process.getType().equals(ProcType.EXECUTE_PLAYER) ? player.getInventory() : miniGame.getPlayerData(player.getUniqueId()).getInventory(name);
                if (isExists) {
                    return inventory == null ? "false" : "true";
                } else if (isEquals) {
                    Process proc2 = processList.get(2);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameInventory = proc2.getType() == ProcType.EXECUTE_GAME;
                    GameData gameData;
                    if (isGameInventory){
                        gameData = miniGame.getGameData();
                    } else {
                        gameData = miniGame.getPlayerData(player.getUniqueId());
                    }
                    return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false";
                } else if (isAdd) {
                    Process proc = processList.get(2);
                    String value = proc.run(miniGame, procUnit);
                    boolean isGameItemStack = proc.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack){
                        GameData gameData = miniGame.getGameData();
                        ItemStack itemStack = gameData.getItemStack(value);
                        inventory.addItem(itemStack);
                    } else if (isAllItemStack) {
                        ItemStack itemStack = ItemStore.getItemStack(value);
                        inventory.addItem(itemStack);
                    } else {
                        ItemStack itemStack = miniGame.getPlayerData(player.getUniqueId()).getItemStack(value);
                        inventory.addItem(itemStack);
                    }
                } else if (isSet) {
                    String proc = processList.get(2).run(miniGame, procUnit);
                    int index = Integer.parseInt(proc);
                    Process proc2 = processList.get(4);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameItemStack = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack){
                        GameData gameData = miniGame.getGameData();
                        ItemStack itemStack = gameData.getItemStack(value);
                        inventory.setItem(index, itemStack);
                    } else if (isAllItemStack) {
                        ItemStack itemStack = ItemStore.getItemStack(value);
                        inventory.setItem(index, itemStack);
                    } else {
                        ItemStack itemStack = miniGame.getPlayerData(player.getUniqueId()).getItemStack(value);
                        inventory.setItem(index, itemStack);
                    }
                } else if (isCreate) {
                    String proc = processList.get(2).run(miniGame, procUnit);
                    try {
                        int index = Integer.parseInt(proc);
                        String title = processList.get(4).run(miniGame, procUnit);
                        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, index, title);
                        miniGame.getPlayerData(player.getUniqueId()).setInventory(name, inv);
                    } catch (Exception e) {
                        String title = processList.get(4).run(miniGame, procUnit);
                        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, InventoryType.valueOf(proc), title);
                        miniGame.getPlayerData(player.getUniqueId()).setInventory(name, inv);
                    }
                } else if (isOpen) {
                    player.openInventory(miniGame.getPlayerData(player.getUniqueId()).getInventory(name));
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

}