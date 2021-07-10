package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.ItemStore;
import kr.jongwonlee.fmg.game.GameData;
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

    SmallFrontBrace frontBrace;
    boolean isGame;
    boolean isSet;
    boolean isType;
    boolean isAdd;
    boolean isLore;
    boolean isRemove;

    @Override
    public ProcType getType() {
        return ProcType.ITEM;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isType = parseUnit.useExecutor(ProcType.EXECUTE_TYPE);
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isLore = parseUnit.useExecutor(ProcType.EXECUTE_LORE);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
        parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        parseUnit.useExecutor(ProcType.EXECUTE_TYPE);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) {
            return "";
        }
        java.util.List<Process> processList = frontBrace.getProcessList();
        String name = processList.get(0).run(miniGame, procUnit);
        Player player = procUnit.target.player;
        if (isGame) {
            if (isAdd) {
                if (isLore) {
                    String value = processList.get(2).run(miniGame, procUnit);
                    ItemStack itemStack = miniGame.getGameData().getItemStack(name);
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
                    ItemStack itemStack = miniGame.getGameData().getItemStack(name);
                    if (player != null) player.getInventory().addItem(itemStack);
                }
            } else if (isSet) {
                if (isLore) {
                    int line = Integer.parseInt(processList.get(2).run(miniGame, procUnit));
                    String value = processList.get(4).run(miniGame, procUnit);
                    ItemStack itemStack = miniGame.getGameData().getItemStack(name);
                    if (isExist(itemStack)) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta == null) itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                        List<String> lore = itemMeta.getLore();
                        if (lore == null) lore = new ArrayList<>();
                        if (line + 1 >= lore.size()) lore.add(value);
                        else if (lore.size() <= line) lore.add(value);
                        else itemMeta.setLore(lore);
                        itemStack.setItemMeta(itemMeta);
                    }
                } else {
                    Process process = processList.get(2);
                    String value = process.run(miniGame, procUnit);
                    boolean isGameItemStack = process.getType() == ProcType.EXECUTE_GAME;
                    boolean isAllItemStack = process.getType() == ProcType.EXECUTE_ONLINE;
                    boolean isTypeItemStack = process.getType() == ProcType.EXECUTE_TYPE;
                    if (isTypeItemStack) {
                        ItemStack itemStack = new ItemStack(Material.getMaterial(value));
                        miniGame.getGameData().setItemStack(name, cloneItemStack(itemStack));
                    } else if (isGameItemStack){
                        GameData gameData = miniGame.getGameData();
                        ItemStack itemStack = gameData.getItemStack(value);
                        gameData.setItemStack(name, cloneItemStack(itemStack));
                    } else if (isAllItemStack) {
                        ItemStack itemStack = ItemStore.getItemStack(value);
                        miniGame.getGameData().setItemStack(name, cloneItemStack(itemStack));
                    } else if (player != null) {
                        ItemStack itemStack = miniGame.getPlayerData(player.getUniqueId()).getItemStack(value);
                        miniGame.getGameData().setItemStack(name, cloneItemStack(itemStack));
                    }
                }
            } else if (isRemove) {
                if (isLore) {
                    int line = Integer.parseInt(processList.get(2).run(miniGame, procUnit));
                    ItemStack itemStack = miniGame.getGameData().getItemStack(name);
                    if (isExist(itemStack)) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta == null) itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                        List<String> lore = itemMeta.getLore();
                        if (lore == null) lore = new ArrayList<>();
                        if (line + 1 >= lore.size()) lore.remove(line);
                        else if (lore.size() <= line) lore.remove(line);
                        else itemMeta.setLore(lore);
                        itemStack.setItemMeta(itemMeta);
                    }
                }
            }
        }
        return "";
    }

    private static boolean isExist(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
    }

    private static ItemStack cloneItemStack(ItemStack itemStack) {
        return itemStack == null ? null : itemStack.clone();
    }

}