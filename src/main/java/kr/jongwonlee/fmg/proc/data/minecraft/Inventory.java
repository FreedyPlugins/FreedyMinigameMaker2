package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.GameDataStore;
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
    private boolean isOnline;
    private boolean isEquals;
    private boolean isExists;
    private boolean isClear;

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
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        isEquals = parseUnit.useExecutor(ProcType.EXECUTE_EQUALS);
        isExists = parseUnit.useExecutor(ProcType.EXECUTE_EXISTS);
        isClear = parseUnit.useExecutor(ProcType.EXECUTE_CLEAR);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) {
            parseUnit.addExecutor(getType());
            return;
        }
        frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) return "";
        Player player = procUnit.target.player;
        List<Process> processList = frontBrace.getProcessList();
        Process process = processList.get(0);
        String name = process.run(miniGame, procUnit);
        try {
            if (isOnline) {
                org.bukkit.inventory.Inventory inventory = GameDataStore.getInst().getInventory(name);
                if (isClear) {
                    inventory.clear();
                    return "";
                } else if (isType) {
                    return inventory.getType().name() + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isSize) {
                    return inventory.getSize() + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isExists) {
                    return inventory == null ? "false" : "true" + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isEquals) {
                    Process proc2 = processList.get(2);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameInventory = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isOnlineInventory = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    if (isOnlineInventory) {
                        GameDataStore gameData = GameDataStore.getInst();
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else if (isGameInventory){
                        GameData gameData = miniGame.getGameData();
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else if (player != null) {
                        GameData gameData = miniGame.getPlayerData(player.getUniqueId());
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    }

                } else if (isAdd) {
                    Process proc = processList.get(2);
                    String value = proc.run(miniGame, procUnit);
                    boolean isGameItemStack = proc.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack){
                        GameData gameData = miniGame.getGameData();
                        ItemStack itemStack = gameData.getItemStack(value);
                        inventory.addItem(itemStack.clone());
                    } else if (isAllItemStack) {
                        ItemStack itemStack = GameDataStore.getInst().getItemStack(value);
                        inventory.addItem(itemStack.clone());
                    } else if (player != null) {
                        ItemStack itemStack = miniGame.getGameData().getItemStack(value);
                        inventory.addItem(itemStack.clone());
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
                        inventory.setItem(index, itemStack.clone());
                    } else if (isAllItemStack) {
                        ItemStack itemStack = GameDataStore.getInst().getItemStack(value);
                        inventory.setItem(index, itemStack.clone());
                    } else if (player != null) {
                        ItemStack itemStack = miniGame.getPlayerData(player.getUniqueId()).getItemStack(value);
                        inventory.setItem(index, itemStack.clone());
                    }
                } else if (isCreate) {
                    String proc = processList.get(2).run(miniGame, procUnit);
                    try {
                        int index = Integer.parseInt(proc);
                        String title = processList.get(4).run(miniGame, procUnit);
                        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, index, title);
                        GameDataStore.getInst().setInventory(name, inv);
                    } catch (Exception e) {
                        String title = processList.get(4).run(miniGame, procUnit);
                        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, InventoryType.valueOf(proc), title);
                        GameDataStore.getInst().setInventory(name, inv);
                    }
                } else if (isOpen) {
                    if (player != null) player.openInventory(GameDataStore.getInst().getInventory(name));
                }
            } else if (isGame) {
                org.bukkit.inventory.Inventory inventory = miniGame.getGameData().getInventory(name);
                if (isClear) {
                    inventory.clear();
                    return "";
                } else if (isType) {
                    return inventory.getType().name() + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isSize) {
                    return inventory.getSize() + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isExists) {
                    return inventory == null ? "false" : "true" + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isEquals) {
                    Process proc2 = processList.get(2);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameInventory = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isOnlineInventory = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    if (isOnlineInventory) {
                        GameDataStore gameData = GameDataStore.getInst();
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else if (isGameInventory) {
                        GameData gameData = miniGame.getGameData();
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else {
                        GameData gameData = miniGame.getPlayerData(player.getUniqueId());
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    }

                } else if (isAdd) {
                    Process proc = processList.get(2);
                    String value = proc.run(miniGame, procUnit);
                    boolean isGameItemStack = proc.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack) {
                        GameData gameData = miniGame.getGameData();
                        ItemStack itemStack = gameData.getItemStack(value);
                        inventory.addItem(itemStack.clone());
                    } else if (isAllItemStack) {
                        ItemStack itemStack = GameDataStore.getInst().getItemStack(value);
                        inventory.addItem(itemStack.clone());
                    } else if (player != null) {
                        ItemStack itemStack = miniGame.getGameData().getItemStack(value);
                        inventory.addItem(itemStack.clone());
                    }
                } else if (isSet) {
                    String proc = processList.get(2).run(miniGame, procUnit);
                    int index = Integer.parseInt(proc);
                    Process proc2 = processList.get(4);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameItemStack = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack) {
                        GameData gameData = miniGame.getGameData();
                        ItemStack itemStack = gameData.getItemStack(value);
                        inventory.setItem(index, itemStack.clone());
                    } else if (isAllItemStack) {
                        ItemStack itemStack = GameDataStore.getInst().getItemStack(value);
                        inventory.setItem(index, itemStack.clone());
                    } else if (player != null) {
                        ItemStack itemStack = miniGame.getPlayerData(player.getUniqueId()).getItemStack(value);
                        inventory.setItem(index, itemStack.clone());
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
                if (isClear) {
                    inventory.clear();
                    return "";
                } else if (isType) {
                    return inventory.getType().name() + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isSize) {
                    return inventory.getSize() + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isExists) {
                    return inventory == null ? "false" : "true" + frontBrace.getLastProc().run(miniGame, procUnit);
                } else if (isEquals) {
                    Process proc2 = processList.get(2);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameInventory = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isOnlineInventory = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    if (isOnlineInventory) {
                        GameDataStore gameData = GameDataStore.getInst();
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else if (isGameInventory) {
                        GameData gameData = miniGame.getGameData();
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else {
                        GameData gameData = miniGame.getPlayerData(player.getUniqueId());
                        return Objects.equals(inventory, gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    }
                } else if (isAdd) {
                    Process proc = processList.get(2);
                    String value = proc.run(miniGame, procUnit);
                    boolean isGameItemStack = proc.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack){
                        GameData gameData = miniGame.getGameData();
                        ItemStack itemStack = gameData.getItemStack(value);
                        inventory.addItem(itemStack.clone());
                    } else if (isAllItemStack) {
                        ItemStack itemStack = GameDataStore.getInst().getItemStack(value);
                        inventory.addItem(itemStack.clone());
                    } else {
                        ItemStack itemStack = miniGame.getPlayerData(player.getUniqueId()).getItemStack(value);
                        inventory.addItem(itemStack.clone());
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
                        inventory.setItem(index, itemStack.clone());
                    } else if (isAllItemStack) {
                        ItemStack itemStack = GameDataStore.getInst().getItemStack(value);
                        inventory.setItem(index, itemStack.clone());
                    } else {
                        ItemStack itemStack = miniGame.getPlayerData(player.getUniqueId()).getItemStack(value);
                        inventory.setItem(index, itemStack.clone());
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
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}