package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Processable(alias = {"item"})
public class Item implements Process {

    Process process;
    SmallFrontBrace frontBrace;
    List<Process> processList;
    ProcType dataType;
    boolean isSet;
    boolean isType;
    boolean isAdd;
    boolean isLore;
    boolean isRemove;
    boolean isName;
    boolean isExists;
    boolean isEquals;
    boolean isSize;
    boolean isCode;
    boolean isGet;

    public static ItemStack clone(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == null ? null : itemStack.clone();
    }

    @Override
    public ProcType getType() {
        return ProcType.ITEM;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        dataType = getGameData(parseUnit);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isType = parseUnit.useExecutor(ProcType.EXECUTE_TYPE);
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isLore = parseUnit.useExecutor(ProcType.EXECUTE_LORE);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        isName = parseUnit.useExecutor(ProcType.NAME);
        isExists = parseUnit.useExecutor(ProcType.EXECUTE_EXISTS);
        isEquals = parseUnit.useExecutor(ProcType.EXECUTE_EQUALS);
        isSize = parseUnit.useExecutor(ProcType.EXECUTE_SIZE);
        isCode = parseUnit.useExecutor(ProcType.EXECUTE_CODE);
        isGet = parseUnit.useExecutor(ProcType.EXECUTE_GET);
        if (isGet) parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
        processList = frontBrace.cutBehindEndBrace();
        parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        parseUnit.useExecutor(ProcType.EXECUTE_TYPE);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace == null) {
                return process.run(miniGame, procUnit);
            }
            Process proc = processList.get(0);
            String name = proc.run(miniGame, procUnit);
            Player player = procUnit.target.player;
            GameData gameData = getGameData(miniGame, procUnit, dataType);
            if (isName) {
                return gameData.getItemStack(name).getItemMeta().getDisplayName()
                        + frontBrace.getLastProc().run(miniGame, procUnit);
            }
            if (isCode) {
                ItemStack itemStack = gameData.getItemStack(name);
                return itemStack == null || itemStack.getType() == Material.AIR ? "0:0" : itemStack.getTypeId() + ":" + itemStack.getData().getData();
            } else if (isEquals) {
                ItemStack itemStack = gameData.getItemStack(name);
                Process proc2 = processList.get(2);
                String value2 = proc2.run(miniGame, procUnit);
                GameData gameData2 = getGameData(miniGame, procUnit, proc2.getType());
                ItemStack itemStack2 = gameData2.getItemStack(value2);
                return itemStack == null ? "false" : itemStack.isSimilar(itemStack2) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
            } else if (isExists) {
                ItemStack itemStack = gameData.getItemStack(name);
                return itemStack == null ? "false" : "true" + frontBrace.getLastProc().run(miniGame, procUnit);
            } else if (isAdd) {
                if (isLore) {
                    String value = processList.get(2).run(miniGame, procUnit);
                    ItemStack itemStack = gameData.getItemStack(name);
                    if (isExist(itemStack)) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta == null) itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                        List<String> lore = itemMeta.getLore();
                        if (lore == null) lore = new ArrayList<>();
                        lore.add(value);
                        itemMeta.setLore(lore);
                        itemStack.setItemMeta(itemMeta);
                    }
                } else {
                    ItemStack itemStack = gameData.getItemStack(name);
                    player.getInventory().addItem(cloneItemStack(itemStack));
                }
            } else if (isSet) {
                if (isSize) {
                    ItemStack itemStack = gameData.getItemStack(name);
                    itemStack.setAmount(Integer.parseInt(processList.get(2).run(miniGame, procUnit)));
                } else if (isName) {
                    String value = processList.get(2).run(miniGame, procUnit);
                    ItemStack itemStack = gameData.getItemStack(name);
                    if (isExist(itemStack)) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta == null) itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                        itemMeta.setDisplayName(value);
                        itemStack.setItemMeta(itemMeta);
                    }
                } else if (isLore) {
                    int line = Integer.parseInt(processList.get(2).run(miniGame, procUnit));
                    String value = processList.get(4).run(miniGame, procUnit);
                    ItemStack itemStack = gameData.getItemStack(name);
                    if (isExist(itemStack)) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta == null) itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                        List<String> lore = itemMeta.getLore();
                        if (lore == null) lore = new ArrayList<>();
                        if (lore.size() > line) lore.set(line, value);
                        else lore.add(value);
                        itemMeta.setLore(lore);
                        itemStack.setItemMeta(itemMeta);
                    }
                } else {
                    Process process = processList.get(2);
                    String value = process.run(miniGame, procUnit);
                    boolean isTypeItemStack = process.getType() == ProcType.EXECUTE_TYPE;
                    boolean isCodeItemStack = process.getType() == ProcType.EXECUTE_CODE;
                    boolean isPlayerItemStack = process.getType() == ProcType.EXECUTE_PLAYER;
                    if (isPlayerItemStack) {
                        if (player != null) {
                            ItemStack itemStack = player.getInventory().getItem(Integer.parseInt(value));
                            gameData.setItemStack(name, cloneItemStack(itemStack));
                        }
                    } else if (isCodeItemStack) {
                        try {
                            byte damage = Byte.parseByte(processList.get(4).run(miniGame, procUnit));
                            ItemStack itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(value)), 1, damage);
                            gameData.setItemStack(name, cloneItemStack(itemStack));
                        } catch (Exception ignored) {
                            ItemStack itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(value)));
                            gameData.setItemStack(name, cloneItemStack(itemStack));
                        }
                    } else if (isTypeItemStack) {
                        ItemStack itemStack = new ItemStack(Material.getMaterial(value));
                        gameData.setItemStack(name, cloneItemStack(itemStack));
                    } else {
                        gameData.setItemStack(name,
                                cloneItemStack(getGameData(miniGame, procUnit, proc.getType()).getItemStack(name)));
                    }
                }
            } else if (isSize) {
                ItemStack itemStack = gameData.getItemStack(name);
                return itemStack.getAmount() + frontBrace.getLastProc().run(miniGame, procUnit);
            } else if (isType) {
                ItemStack itemStack = gameData.getItemStack(name);
                return itemStack.getType().name() + frontBrace.getLastProc().run(miniGame, procUnit);
            } else if (isRemove) {
                if (isLore) {
                    int line = Integer.parseInt(processList.get(2).run(miniGame, procUnit));
                    ItemStack itemStack = gameData.getItemStack(name);
                    if (isExist(itemStack)) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta == null) itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                        List<String> lore = itemMeta.getLore();
                        if (lore != null && !lore.isEmpty()) {
                            if (line + 1 >= lore.size()) lore.remove(line);
                            else if (lore.size() <= line) lore.remove(lore.size() - 1);
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

    private static boolean isExist(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
    }

    private static ItemStack cloneItemStack(ItemStack itemStack) {
        return itemStack == null ? null : itemStack.clone();
    }

}