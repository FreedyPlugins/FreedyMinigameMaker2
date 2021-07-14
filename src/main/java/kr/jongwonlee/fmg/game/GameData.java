package kr.jongwonlee.fmg.game;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    public GameData(Map<String, String> dataMap,
                    Map<String, Location> locationMap,
                    Map<String, BlockState> blockMap,
                    Map<String, ItemStack> itemStackMap,
                    Map<String, Inventory> inventoryMap,
                    Map<String, List<String>> listMap) {
        this.dataMap = dataMap;
        this.locationMap = locationMap;
        this.blockMap = blockMap;
        this.itemStackMap = itemStackMap;
        this.inventoryMap = inventoryMap;
        this.listMap = listMap;
    }

    public GameData() {
        this.dataMap = new HashMap<>();
        this.locationMap = new HashMap<>();
        this.blockMap = new HashMap<>();
        this.itemStackMap = new HashMap<>();
        this.inventoryMap = new HashMap<>();
        this.listMap = new HashMap<>();
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

    public void setData(String key, String value) {
        if (value == null) dataMap.remove(key);
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
