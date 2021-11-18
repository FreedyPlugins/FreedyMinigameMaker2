package kr.jongwonlee.fmg.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

public class YamlStore extends FileStore {

    FileConfiguration config;

    public YamlStore(String fileName) {
        super(fileName);
        reload();
    }

    public void reload() {
        try {
            if (!file.exists()) loadResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            if (!file.exists()) {
                loadResource();
                this.config = YamlConfiguration.loadConfiguration(this.file);
            }
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationSection getSection(String path) {
        return this.getConfig().getConfigurationSection(path);
    }

    public Set<String> getKeys(ConfigurationSection section) {
        return getKeys(section, false);
    }

    public Set<String> getKeys(ConfigurationSection section, boolean deep) {
        return section == null ? Collections.emptySet() : section.getKeys(deep);
    }

    public Set<String> getKeys(String path) {
        return getKeys(getSection(path));
    }

    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return getConfig().getIntegerList(path);
    }

    public String getString(String path) {
        return getConfig().getString(path);
    }

    public int getInt(String path) { return getConfig().getInt(path); }

    public long getLong(String path) { return getConfig().getLong(path); }

    public double getDouble(String path) { return getConfig().getDouble(path); }

    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public void set(String path, Object value) {
        if (path.length() == 0) return;
        getConfig().set(path, value);
    }

    public boolean isSet(String path) {
        return getConfig().isSet(path);
    }

    public ItemStack getItemStack(String path) {
        return getConfig().getItemStack(path);
    }

    public void setLocation(String path, Location location) {
        if (location == null) {
            set(path, null);
            return;
        }
        set(path + ".WORLD", location.getWorld().getName());
        set(path + ".X", location.getX());
        set(path + ".Y", location.getY());
        set(path + ".Z", location.getZ());
        set(path + ".YAW", location.getYaw());
        set(path + ".PITCH", location.getPitch());
    }

    public Location getLocation(String path) {
        final World world = Bukkit.getWorld(getString(path + ".WORLD"));
        if (world == null) return null;
        final double x = getDouble(path + ".X");
        final double y = getDouble(path + ".Y");
        final double z = getDouble(path + ".Z");
        final double yaw = getDouble(path + ".YAW");
        final double pitch = getDouble(path + ".PITCH");
        return new Location(world, x, y, z, ((float) yaw), ((float) pitch));
    }

    public Map<String, ItemStack> getItemStackMap(String path) {
        final Map<String, ItemStack> itemStackMap = new HashMap<>();
        for (String string : getKeys(path))
            itemStackMap.put(string, getItemStack(path + DOT + string));
        return itemStackMap;
    }

    public Map<String, Location> getLocationMap(String path) {
        final Map<String, Location> locationMap = new HashMap<>();
        for (String string : getKeys(path))
            locationMap.put(string, getLocation(path + DOT + string));
        return locationMap;
    }

    public Map<String, String> getStringMap(String path) {
        final Map<String, String> stringMap = new HashMap<>();
        ConfigurationSection section = getSection(path);
        for (String string : getKeys(section, true)) {
            if (section.getConfigurationSection(string) != null) continue;
            stringMap.put(string, getString(path + DOT + string));
        }
        return stringMap;
    }

    public Map<String, Inventory> getInventoryMap(String path) {
        final Map<String, Inventory> inventoryMap = new HashMap<>();
        for (String string : getKeys(getSection(path), false)) {
            try {
                String typeString = getString(path + DOT + string + DOT + "TYPE");
                String title = getString(path + DOT + string + DOT + "TITLE");
                InventoryType inventoryType = typeString == null ? InventoryType.CHEST : InventoryType.valueOf(typeString);
                int size = getInt(path + DOT + string + DOT + "SIZE");
                Inventory inventory;
                if (inventoryType == InventoryType.CHEST) inventory = Bukkit.createInventory(null, size, title == null ? "" : title);
                else if (inventoryType == InventoryType.PLAYER) inventory = Bukkit.createInventory(null, InventoryType.PLAYER, InventoryType.PLAYER.getDefaultTitle());
                else inventory = Bukkit.createInventory(null, inventoryType, title == null ? "" : title);
                Map<String, ItemStack> itemStackMap = getItemStackMap(path + DOT + string + DOT + "ITEMS");
                itemStackMap.forEach((s, itemStack) -> inventory.setItem(Integer.parseInt(s), itemStack));
                inventoryMap.put(string, inventory);
            } catch (Exception ignored) { }
        }
        return inventoryMap;
    }

    public Map<String, List<String>> getListMap(String path) {
        final Map<String, List<String>> stringMap = new HashMap<>();
        for (String string : getKeys(getSection(path), false))
            stringMap.put(string, getStringList(path + DOT + string));
        return stringMap;
    }

    public void setInventory(String path, Inventory inventory) {
        if (inventory == null) {
            set(path, null);
            return;
        }
        InventoryType type = inventory.getType();
        set(path + DOT + "TYPE", type == InventoryType.CHEST ? null : type.name());
        set(path + DOT + "TITLE", inventory.getType() == InventoryType.PLAYER ? InventoryType.PLAYER.getDefaultTitle() : inventory.getTitle());
        set(path + DOT + "SIZE", inventory.getSize());
        ItemStack[] contents = inventory.getStorageContents();
        for (int i = 0; i < contents.length; i++) set(path + DOT + "ITEMS" + DOT + i, contents[i]);
    }

    public void setList(String path, List<String> stringList) {
        if (stringList == null || stringList.size() == 0) set(path, null);
        else set(path, stringList);
    }

    public void reset(boolean doInit) {
        remove();
        create(doInit);
        reload();
    }

    public void setEmpty() {
        config = new YamlConfiguration();
    }

}
