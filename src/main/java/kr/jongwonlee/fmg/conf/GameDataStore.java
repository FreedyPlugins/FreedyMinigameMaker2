package kr.jongwonlee.fmg.conf;

import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.util.YamlStore;
import org.bukkit.event.Listener;

import java.util.Map;

public class GameDataStore extends GameData implements Listener {

    private static GameDataStore instance;
    private static final YamlStore dataStore = new YamlStore("data.yml");
    private static final YamlStore itemStackStore = new YamlStore("items.yml");
    private static final YamlStore locationStore = new YamlStore("locations.yml");
    private static final YamlStore inventoryStore = new YamlStore("inventories.yml");
    private static final YamlStore listStore = new YamlStore("lists.yml");
//    private static final YamlStore blockStore = new YamlStore("blocks.yml");

    public GameDataStore() {
        super(
                dataStore.getStringMap(""),
                locationStore.getLocationMap(""),
                itemStackStore.getItemStackMap(""),
                inventoryStore.getInventoryMap(""),
                listStore.getListMap(""));
    }

    public static GameDataStore getInst() {
        return instance;
    }

    public static void init() {
        instance = new GameDataStore();
    }

    public static void save() {
        GameDataStore inst = getInst();
        set(inst.dataMap, dataStore::set, dataStore);
        set(inst.locationMap, locationStore::setLocation, locationStore);
        set(inst.itemStackMap, itemStackStore::set, itemStackStore);
        set(inst.inventoryMap, inventoryStore::setInventory, inventoryStore);
        set(inst.listMap, listStore::setList, listStore);
    }

    @FunctionalInterface
    public interface Setter<T> {
        void run(String string, T t);
    }

    public static <T> void set(Map<String, T> map, Setter<T> setter, YamlStore yamlStore) {
        try {
            yamlStore.setEmpty();
            map.forEach(setter::run);
            yamlStore.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
