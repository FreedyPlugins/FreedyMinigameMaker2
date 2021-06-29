package kr.jongwonlee.fmg.conf;

import kr.jongwonlee.fmg.util.YamlStore;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemStore {

    private static final String ROOT = "item.";
    private static Map<String, ItemStack> itemMap = new HashMap<>();

    public static void init() {
        YamlStore yamlStore = new YamlStore("items.yml");
        itemMap = yamlStore.getItemStackMap(ROOT);
    }

    public static ItemStack getItemStack(String name) {
        return itemMap.getOrDefault(ROOT + name, null);
    }

    public static void setItemStack(String name, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            itemMap.remove(name);
            YamlStore yamlStore = new YamlStore("items.yml");
            yamlStore.set(ROOT + name, null);
            yamlStore.save();
        } else {
            itemMap.put(name, itemStack);
            YamlStore yamlStore = new YamlStore("items.yml");
            yamlStore.set(ROOT + name, itemStack);
            yamlStore.save();
        }
    }

}
