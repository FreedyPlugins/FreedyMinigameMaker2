package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Processable(alias = {"inventory", "inven", "inv"})
public class Inventory implements Process {

    private SmallFrontBrace frontBrace;
    List<Process> processList;
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
    private boolean isClone;
    private boolean isRemove;
    private boolean isClose;
    private boolean isItem;

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
        isClone = parseUnit.useExecutor(ProcType.EXECUTE_CLONE);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        isClose = parseUnit.useExecutor(ProcType.EXECUTE_CLOSE);
        isItem = parseUnit.useExecutor(ProcType.ITEM);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) {
            parseUnit.addExecutor(getType());
            return;
        }
        frontBrace = ((SmallFrontBrace) process);
        processList = frontBrace.cutBehindEndBrace();
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        Player player = procUnit.target.player;
        if (isClose) {
            player.closeInventory();
        }
        if (frontBrace == null) return "";
        Process process = processList.get(0);
        String name = process.run(miniGame, procUnit);
        try {
            if (isOnline) {
                org.bukkit.inventory.Inventory inventory = GameDataStore.getInst().getInventory(name);
                if (process.getType() == ProcType.EXECUTE_PLAYER) inventory = player.getInventory();
                if (isItem) {
                    Process proc1 = processList.get(2);
                    int index;
                    if (proc1.getType() != ProcType.EXECUTE_HOT_BAR) {
                        String proc = proc1.run(miniGame, procUnit);
                        index = Integer.parseInt(proc);
                    } else index = player.getInventory().getHeldItemSlot();
                    Process proc2 = processList.get(4);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameItemStack = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack){
                        GameData gameData = miniGame.getGameData();
                        gameData.setItemStack(value, Item.clone(inventory.getItem(index)));
                    } else if (isAllItemStack) {
                        GameData gameData = GameDataStore.getInst();
                        gameData.setItemStack(value, Item.clone(inventory.getItem(index)));
                    } else if (player != null) {
                        GameData gameData = GameStore.getPlayerData(player.getUniqueId());
                        gameData.setItemStack(value, Item.clone(inventory.getItem(index)));
                    }
                } else if (isRemove) GameDataStore.getInst().setInventory(name, null);
                else if (isClear) {
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
                        return inventory.equals(gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else if (isGameInventory){
                        GameData gameData = miniGame.getGameData();
                        return inventory.equals(gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else if (player != null) {
                        GameData gameData = GameStore.getPlayerData(player.getUniqueId());
                        return inventory.equals(gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    }
                } else if (isClone) {
                    Process proc2 = processList.get(2);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameInventory = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isOnlineInventory = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    boolean isPlayerInventory = proc2.getType() == ProcType.EXECUTE_PLAYER;
                    GameData gameData;
                    if (isOnlineInventory) gameData = GameDataStore.getInst();
                    else if (isGameInventory) gameData = miniGame.getGameData();
                    else if (isPlayerInventory) gameData = null;
                    else gameData = GameStore.getPlayerData(player.getUniqueId());
                    org.bukkit.inventory.Inventory inv;
                    if (process.getType() == ProcType.EXECUTE_PLAYER) {
                        inventory = player.getInventory();
                    }
                    if (gameData == null) {
                        inv = player.getInventory();
                    }
                    else if (inventory.getType() == InventoryType.PLAYER)
                        inv = Bukkit.createInventory(null, InventoryType.PLAYER, "");
                    else if (inventory.getType() != InventoryType.CHEST)
                        inv = Bukkit.createInventory(null, inventory.getType(), inventory.getTitle());
                    else
                        inv = Bukkit.createInventory(null, inventory.getSize(), inventory.getTitle());
                    ItemStack[] contents = inventory.getContents();
                    ItemStack[] itemStacks = contents.clone();
                    for (int i = 0; i < itemStacks.length; i++) itemStacks[i] = contents[i] == null ? null : contents[i].clone();
                    inv.setContents(itemStacks);
                    if (gameData != null) gameData.setInventory(value, inv);
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
                        ItemStack itemStack = GameStore.getPlayerData(player.getUniqueId()).getItemStack(value);
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
                if (process.getType() == ProcType.EXECUTE_PLAYER) inventory = player.getInventory();
                if (isItem) {
                    Process proc1 = processList.get(2);
                    int index;
                    if (proc1.getType() != ProcType.EXECUTE_HOT_BAR) {
                        String proc = proc1.run(miniGame, procUnit);
                        index = Integer.parseInt(proc);
                    } else index = player.getInventory().getHeldItemSlot();
                    Process proc2 = processList.get(4);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameItemStack = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack){
                        GameData gameData = miniGame.getGameData();
                        gameData.setItemStack(value, Item.clone(inventory.getItem(index)));
                    } else if (isAllItemStack) {
                        GameData gameData = GameDataStore.getInst();
                        gameData.setItemStack(value, Item.clone(inventory.getItem(index)));
                    } else if (player != null) {
                        GameData gameData = GameStore.getPlayerData(player.getUniqueId());
                        gameData.setItemStack(value, Item.clone(inventory.getItem(index)));
                    }
                } else if (isRemove) miniGame.getGameData().setInventory(name, null);
                else if (isClear) {
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
                        return inventory.equals(gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else if (isGameInventory) {
                        GameData gameData = miniGame.getGameData();
                        return inventory.equals(gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else {
                        GameData gameData = GameStore.getPlayerData(player.getUniqueId());
                        return inventory.equals(gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    }

                } else if (isClone) {
                    Process proc2 = processList.get(2);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameInventory = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isOnlineInventory = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    boolean isPlayerInventory = proc2.getType() == ProcType.EXECUTE_PLAYER;
                    GameData gameData;
                    if (isOnlineInventory) gameData = GameDataStore.getInst();
                    else if (isGameInventory) gameData = miniGame.getGameData();
                    else if (isPlayerInventory) gameData = null;
                    else gameData = GameStore.getPlayerData(player.getUniqueId());
                    org.bukkit.inventory.Inventory inv;
                    if (process.getType() == ProcType.EXECUTE_PLAYER) {
                        inventory = player.getInventory();
                    }
                    if (gameData == null) {
                        inv = player.getInventory();
                    }
                    else if (inventory.getType() == InventoryType.PLAYER)
                        inv = Bukkit.createInventory(null, InventoryType.PLAYER, "");
                    else if (inventory.getType() != InventoryType.CHEST)
                        inv = Bukkit.createInventory(null, inventory.getType(), inventory.getTitle());
                    else
                        inv = Bukkit.createInventory(null, inventory.getSize(), inventory.getTitle());
                    ItemStack[] contents = inventory.getContents();
                    ItemStack[] itemStacks = contents.clone();
                    for (int i = 0; i < itemStacks.length; i++) itemStacks[i] = contents[i] == null ? null : contents[i].clone();
                    inv.setContents(itemStacks);
                    if (gameData != null) gameData.setInventory(value, inv);
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
                        ItemStack itemStack = GameStore.getPlayerData(player.getUniqueId()).getItemStack(value);
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
                org.bukkit.inventory.Inventory inventory = process.getType().equals(ProcType.EXECUTE_PLAYER) ? player.getInventory() : GameStore.getPlayerData(player.getUniqueId()).getInventory(name);
                if (process.getType() == ProcType.EXECUTE_PLAYER) inventory = player.getInventory();
                if (isItem) {
                    Process proc1 = processList.get(2);
                    int index;
                    if (proc1.getType() != ProcType.EXECUTE_HOT_BAR) {
                        String proc = proc1.run(miniGame, procUnit);
                        index = Integer.parseInt(proc);
                    } else index = player.getInventory().getHeldItemSlot();
                    Process proc2 = processList.get(4);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameItemStack = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    if (isGameItemStack) {
                        GameData gameData = miniGame.getGameData();
                        gameData.setItemStack(value, Item.clone(inventory.getItem(index)));
                    } else if (isAllItemStack) {
                        GameData gameData = GameDataStore.getInst();
                        gameData.setItemStack(value, Item.clone(inventory.getItem(index)));
                    } else {
                        GameData gameData = GameStore.getPlayerData(player.getUniqueId());
                        gameData.setItemStack(value, Item.clone(inventory.getItem(index)));
                    }
                } else if (isRemove) GameStore.getPlayerData(player.getUniqueId()).setInventory(name, null);
                else if (isClear) {
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
                        return inventory.equals(gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else if (isGameInventory) {
                        GameData gameData = miniGame.getGameData();
                        return inventory.equals(gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    } else {
                        GameData gameData = GameStore.getPlayerData(player.getUniqueId());
                        return inventory.equals(gameData.getInventory(value)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                    }
                } else if (isClone) {
                    Process proc2 = processList.get(2);
                    String value = proc2.run(miniGame, procUnit);
                    boolean isGameInventory = proc2.getType() == ProcType.EXECUTE_GAME;
                    boolean isOnlineInventory = proc2.getType() == ProcType.EXECUTE_ONLINE;
                    boolean isPlayerInventory = proc2.getType() == ProcType.EXECUTE_PLAYER;
                    GameData gameData;
                    if (isOnlineInventory) gameData = GameDataStore.getInst();
                    else if (isGameInventory) gameData = miniGame.getGameData();
                    else if (isPlayerInventory) gameData = null;
                    else gameData = GameStore.getPlayerData(player.getUniqueId());
                    org.bukkit.inventory.Inventory inv;
                    if (process.getType() == ProcType.EXECUTE_PLAYER) {
                        inventory = player.getInventory();
                    }
                    if (gameData == null) {
                        inv = player.getInventory();
                    }
                    else if (inventory.getType() == InventoryType.PLAYER)
                        inv = Bukkit.createInventory(null, InventoryType.PLAYER, InventoryType.PLAYER.getDefaultTitle());
                    else if (inventory.getType() != InventoryType.CHEST)
                        inv = Bukkit.createInventory(null, inventory.getType(), inventory.getTitle());
                    else
                        inv = Bukkit.createInventory(null, inventory.getSize(), inventory.getTitle());
                    ItemStack[] contents = inventory.getContents();
                    ItemStack[] itemStacks = contents.clone();
                    for (int i = 0; i < itemStacks.length; i++) itemStacks[i] = contents[i] == null ? null : contents[i].clone();
                    inv.setContents(itemStacks);
                    if (gameData != null) gameData.setInventory(value, inv);
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
                        ItemStack itemStack = GameStore.getPlayerData(player.getUniqueId()).getItemStack(value);
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
                        ItemStack itemStack = GameStore.getPlayerData(player.getUniqueId()).getItemStack(value);
                        inventory.setItem(index, itemStack.clone());
                    }
                } else if (isCreate) {
                    String proc = processList.get(2).run(miniGame, procUnit);
                    try {
                        int index = Integer.parseInt(proc);
                        String title = processList.get(4).run(miniGame, procUnit);
                        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, index, title);
                        GameStore.getPlayerData(player.getUniqueId()).setInventory(name, inv);
                    } catch (Exception e) {
                        String title = processList.get(4).run(miniGame, procUnit);
                        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, InventoryType.valueOf(proc), title);
                        GameStore.getPlayerData(player.getUniqueId()).setInventory(name, inv);
                    }
                } else if (isOpen) {
                    player.openInventory(GameStore.getPlayerData(player.getUniqueId()).getInventory(name));
                }
            }
        } catch (Exception e) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}