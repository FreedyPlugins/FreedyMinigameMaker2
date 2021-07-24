package kr.jongwonlee.fmg.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.boss.BossBar;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameData {

    protected Map<String, String> dataMap;
    protected Map<String, Location> locationMap;
    protected Map<String, BlockState> blockMap;
    protected Map<String, ItemStack> itemStackMap;
    protected Map<String, Inventory> inventoryMap;
    protected Map<String, List<String>> listMap;
    protected Map<String, BossBar> bossBarMap;
    private List<Integer> taskIdList;

    public GameData(Map<String, String> dataMap,
                    Map<String, Location> locationMap,
                    Map<String, ItemStack> itemStackMap,
                    Map<String, Inventory> inventoryMap,
                    Map<String, List<String>> listMap) {
        this.dataMap = dataMap;
        this.locationMap = locationMap;
        this.blockMap = new HashMap<>();
        this.itemStackMap = itemStackMap;
        this.inventoryMap = inventoryMap;
        this.listMap = listMap;
        this.bossBarMap = new HashMap<>();
        this.taskIdList = new ArrayList<>();
    }

    public GameData() {
        this.dataMap = new HashMap<>();
        this.locationMap = new HashMap<>();
        this.blockMap = new HashMap<>();
        this.itemStackMap = new HashMap<>();
        this.inventoryMap = new HashMap<>();
        this.listMap = new HashMap<>();
        this.bossBarMap = new HashMap<>();
        taskIdList = new ArrayList<>();
    }

    public Location getLocation(String key) {
        return locationMap.getOrDefault(key, null);
    }

    public BlockState getBlock(String key) {
        return blockMap.getOrDefault(key, null);
    }

    public ItemStack getItemStack(String key) {
        return itemStackMap.getOrDefault(key, null);
    }

    public Inventory getInventory(String key) {
        return inventoryMap.getOrDefault(key, null);
    }

    public List<String> getList(String key) {
        return listMap.getOrDefault(key, null);
    }

    public void setLocation(String key, Location location) {
        locationMap.put(key, location);
    }

    public void setBlock(String key, BlockState blockState) {
        blockMap.put(key, blockState);
    }

    public void setItemStack(String key, ItemStack itemStack) {
        itemStackMap.put(key, itemStack);
    }

    public void setInventory(String key, Inventory inventory) {
        inventoryMap.put(key, inventory);
    }

    public void setList(String key, List<String> list) {
        listMap.put(key, list);
    }

    public String getData(String key) {
        return dataMap.getOrDefault(key, "null");
    }

    public void setBossBar(String key, BossBar bossBar) {
        bossBarMap.put(key, bossBar);
    }

    public BossBar getBossBar(String key) {
        return bossBarMap.getOrDefault(key, null);
    }

    public void addTaskId(int taskId) {
        taskIdList.add(taskId);
    }

    public void cancelTaskAll() {
        new ArrayList<>(taskIdList).forEach(taskId -> Bukkit.getScheduler().cancelTask(taskId));
        taskIdList = new ArrayList<>();
    }

    public void setData(String key, String value) {
        if (value == null) dataMap.remove(key);
        else if (value.equals("null")) dataMap.remove(key);
        else dataMap.put(key, value);
    }

    public void clear() {
        this.dataMap = new HashMap<>();
        this.locationMap = new HashMap<>();
        this.blockMap = new HashMap<>();
        this.itemStackMap = new HashMap<>();
        this.inventoryMap = new HashMap<>();
        this.listMap = new HashMap<>();
    }

}
