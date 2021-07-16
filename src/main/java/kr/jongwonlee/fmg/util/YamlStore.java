package kr.jongwonlee.fmg.util;

import kr.jongwonlee.fmg.FMGPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

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

    public void setItemStackMap(String path, Map<String, ItemStack> itemStackMap) {
        itemStackMap.forEach((key, itemStack) -> getConfig().set(path + DOT + key, itemStack));
    }

    public void setInventoryMap(String path, Map<String, Inventory> inventoryMap) {
        inventoryMap.forEach((key, inventory) -> setInventory(path + DOT + key, inventory));
    }

    public void setItemStackList(String path, List<ItemStack> itemStackList) {
        for (int i = 0; i < itemStackList.size(); i++) getConfig().set(path + DOT + i, itemStackList.get(i));
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
        getConfig().set(path, value);
    }

    public boolean isSet(String path) {
        return getConfig().isSet(path);
    }

    public ItemStack getItemStack(String path) {
        return getConfig().getItemStack(path);
    }

    public String[] getShape(String path) {
        return getString(path).replace(" ", "").replace("\n", "").split("");
    }

    public static List<String> getDirFiles(String otherPath) {
        final File file = new File(FMGPlugin.getInst().getDataFolder().getAbsolutePath(), DIR + otherPath);
        file.mkdirs();
        final Path path = file.toPath();
        try {
            return Files.walk(path)
                    .filter(f -> !Files.isDirectory(f))
                    .map(p -> {
                        final String string = path.relativize(p).toString();
                        return string.substring(0, string.lastIndexOf('.'));
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void remove() {
        file.delete();
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
        for (String string : getKeys(getSection(path), true))
            stringMap.put(string, getString(path + DOT + string));
        return stringMap;
    }

    public Map<String, Inventory> getInventoryMap(String path) {
        final Map<String, Inventory> inventoryMap = new HashMap<>();
        for (String string : getKeys(getSection(path), false)) {
            try {
                String typeString = getString(path + DOT + "TYPE");
                String title = getString(path + DOT + "TITLE");
                InventoryType inventoryType = typeString == null ? null : InventoryType.valueOf(typeString);
                int size = getInt(path + DOT + "SIZE");
                Inventory inventory;
                if (inventoryType == null) inventory = Bukkit.createInventory(null, size, title == null ? "" : title);
                else inventory = Bukkit.createInventory(null, inventoryType, title == null ? "" : title);
                Map<String, ItemStack> itemStackMap = getItemStackMap(path + DOT + "ITEMS");
                itemStackMap.forEach((s, itemStack) -> inventory.setItem(Integer.parseInt(s), itemStack));
                inventoryMap.put(string, inventory);
            } catch (Exception ignored) { }
        }
        return inventoryMap;
    }

    public Map<String, List<String>> getListMap(String path) {
        final Map<String, List<String>> stringMap = new HashMap<>();
        for (String string : getKeys(getSection(path), true))
            stringMap.put(string, getStringList(path + DOT + string));
        return stringMap;
    }

    public void setInventory(String path, Inventory inventory) {
        InventoryType type = inventory.getType();
        set(path + DOT + "TYPE", type == InventoryType.CHEST ? null : type);
        set(path + DOT + "TITLE", inventory.getTitle());
        set(path + DOT + "SIZE", inventory.getSize());
        ItemStack[] contents = inventory.getStorageContents();
        for (int i = 0; i < contents.length; i++) set(path + DOT + "ITEMS" + DOT + i, contents[i]);
    }

}
