package kr.jongwonlee.fmg.conf;

import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.util.YamlStore;
import org.bukkit.event.Listener;

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
        dataStore.reset(false);
        locationStore.reset(false);
        itemStackStore.reset(false);
        inventoryStore.reset(false);
        listStore.reset(false);
        inst.dataMap.forEach(dataStore::set);
        inst.locationMap.forEach(locationStore::setLocation);
        inst.itemStackMap.forEach(itemStackStore::set);
        inst.inventoryMap.forEach(inventoryStore::setInventory);
        inst.listMap.forEach(listStore::setList);
        dataStore.save();
        itemStackStore.save();
        locationStore.save();
        inventoryStore.save();
        listStore.save();
    }

}
