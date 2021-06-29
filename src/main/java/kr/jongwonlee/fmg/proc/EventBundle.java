package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.util.GameAlert;
import kr.jongwonlee.fmg.util.YamlStore;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.stream.Collectors;

public enum EventBundle {

    JOIN,
    LEFT,
    PRE_GAME_JOIN,
    GAME_JOIN,
    PRE_GAME_LEFT,
    GAME_LEFT,
    GAME_START,
    PRE_GAME_STOP,
    GAME_STOP,
    INTERACT,
    MOVE,
/*
    INTERACT_ENTITY("onInteractEntity"),
    ITEM_CONSUME("onItemConsume"),
    INVENTORY_CLICK("onInventoryClick"),
    BLOCK_BREAK("onBlockBreak"),
    BLOCK_PLACE("onBlockPlace"),
    BLOCK_DAMAGE("onBlockDamage"),
    DEATH("onDeath"),
    DAMAGE_ENTITY("onDamageEntity"),
    DAMAGED("onDamaged"),
    PROJECTILE("onProjectile"),
    PROJECTILE_HIT("onProjectileHit"),
    ITEM_HELD("onItemHeld"),
    SWAP_HAND("onSwapHand"),
    DROP("onDrop"),
    PICKUP("onPickup"),
    CHAT("onChat"),
    COMMAND("onCommand"),
    WORLD_CHANGE("onWorldChange"),
    FISHING_THROW("onFishingThrow"),
    FISHING_BACK("onFishingBack"),
    VEHICLE_DAMAGE("onVehicleDamage"),
    VEHICLE_EXIT("onVehicleExit"),
*/
    ;

    private String name;

    public String getName() {
        return name;
    }

    @Deprecated
    public static EventBundle getEventBundle(String name) {
        try {
            return Arrays.stream(EventBundle.values()).filter(eventBundle -> Objects.equals(eventBundle.name, name)).collect(Collectors.toList()).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public static void init() {
        YamlStore yamlStore = new YamlStore("events.yml");
        List<String> stringList = new ArrayList<>();
        for (EventBundle eventBundle : EventBundle.values()) {
            String eventString = yamlStore.getString(eventBundle.name());
            if (eventString == null) continue;
            if (stringList.contains(eventString)) GameAlert.DUPLICATED_EVENT.print(new String[]{eventBundle.name()});
            else eventBundle.name = eventString.toLowerCase();
            stringList.add(eventString);
        }
    }

}
