package kr.jongwonlee.fmg;

import com.eatthepath.uuid.FastUUID;
import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.EventBundle;
import kr.jongwonlee.fmg.proc.ProcTarget;
import kr.jongwonlee.fmg.proc.ProcUnit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.projectiles.ProjectileSource;

import java.util.stream.Collectors;

public class FMGListener implements Listener {

    public static void init() {
        FMGPlugin.registerEvent(new FMGListener());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setData("joinMessage", event.getJoinMessage());
        game.run(EventBundle.JOIN, player);
        String joinMessage = playerData.getData("joinMessage");
        if (joinMessage.equals("null")) event.setJoinMessage(null);
        else event.setJoinMessage(joinMessage);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setData("leftMessage", event.getQuitMessage());
        game.run(EventBundle.LEFT, player);
        String joinMessage = playerData.getData("leftMessage");
        if (joinMessage.equals("null")) event.setQuitMessage(null);
        else event.setQuitMessage(joinMessage);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setLocation("moveFrom", event.getFrom().clone());
        playerData.setLocation("moveTo", event.getTo().clone());
        String result = game.run(EventBundle.MOVE, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setData("interactAction", event.getAction().name());
        EquipmentSlot hand = event.getHand();
        playerData.setData("interactHand", hand == null ? "null" : hand.name());
        playerData.setData("interactBlockFace", event.getBlockFace().name());
        Block clickedBlock = event.getClickedBlock();
        playerData.setBlock("interactBlock", clickedBlock == null ? null : clickedBlock.getState());
        playerData.setItemStack("interactItem", event.getItem());
        String result = GameStore.getGame(player).run(EventBundle.INTERACT, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        try {
            Player player = event.getPlayer();
            if (!player.isOnline()) return;
            MiniGame game = GameStore.getGame(player);
            GameData playerData = game.getPlayerData(player.getUniqueId());
            playerData.setData("chat", event.getMessage());
            String result = GameStore.getGame(player).run(EventBundle.CHAT, player);
            if (result.equals("false")) event.setCancelled(true);
        } catch (Exception ignored) {  }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setLocation("teleportFrom", event.getFrom());
        playerData.setLocation("teleportTo", event.getTo());
        String result = GameStore.getGame(player).run(EventBundle.TELEPORT, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setData("command", event.getMessage());
        String result = GameStore.getGame(player).run(EventBundle.COMMAND, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) return;
        Player player = ((Player) humanEntity);
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setInventory("inventoryClicked", event.getClickedInventory());
        playerData.setData("inventoryHotBar", String.valueOf(event.getHotbarButton()));
        playerData.setItemStack("inventoryCursor", event.getCursor());
        playerData.setItemStack("inventoryCurrentItem", event.getCurrentItem());
        playerData.setData("inventoryAction", event.getAction().name());
        playerData.setData("inventoryClick", event.getClick().name());
        playerData.setData("inventoryRawSlot", String.valueOf(event.getRawSlot()));
        playerData.setData("inventorySlotType", event.getSlotType().name());
        playerData.setData("inventorySlot", String.valueOf(event.getSlot()));
        String result = GameStore.getGame(player).run(EventBundle.INVENTORY_CLICK, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) return;
        Player player = ((Player) humanEntity);
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setInventory("inventoryDrag", event.getInventory());
        playerData.setList("inventorySlots", event.getInventorySlots().stream().map(String::valueOf).collect(Collectors.toList()));
        playerData.setList("inventoryRawSlots", event.getRawSlots().stream().map(String::valueOf).collect(Collectors.toList()));
        playerData.setList("inventoryDragItems", event.getNewItems().keySet().stream().map(String::valueOf).collect(Collectors.toList()));
        event.getNewItems().forEach((i, itemStack) -> playerData.setItemStack("inventoryDragItem_" + i, itemStack));
        playerData.setItemStack("inventoryCursor", event.getCursor());
        playerData.setItemStack("inventoryOldCursor", event.getOldCursor());
        playerData.setData("inventoryDragType", event.getType().name());
        String result = GameStore.getGame(player).run(EventBundle.INVENTORY_DRAG, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity humanEntity = event.getPlayer();
        if (!(humanEntity instanceof Player)) return;
        Player player = ((Player) humanEntity);
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setInventory("inventoryClosed", event.getInventory());
        GameStore.getGame(player).run(EventBundle.INVENTORY_CLOSE, player);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = ((Player) entity);
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setData("damage", String.valueOf(event.getDamage()));
        playerData.setData("damageCause", event.getCause().name());
        playerData.setData("damageFinal", String.valueOf(event.getFinalDamage()));
        String result = GameStore.getGame(player).run(EventBundle.PLAYER_DAMAGE, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setItemStack("dropItem", event.getItemDrop().getItemStack());
        String result = GameStore.getGame(player).run(EventBundle.DROP_ITEM, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity entity = event.getEntity();
        {
            Player player;
            if (attacker instanceof Player) player = (Player) attacker;
            else if (attacker instanceof Projectile) {
                Projectile projectile = (Projectile) attacker;
                ProjectileSource shooter = projectile.getShooter();
                if (shooter instanceof Player) player = (Player) projectile.getShooter();
                else player = null;
            } else player = null;
            if (player != null) {
                if (!player.isOnline()) return;
                MiniGame game = GameStore.getGame(player);
                GameData playerData = game.getPlayerData(player.getUniqueId());
                playerData.setData("damage", String.valueOf(event.getDamage()));
                playerData.setData("damageCause", event.getCause().name());
                playerData.setData("damageFinal", String.valueOf(event.getFinalDamage()));
                playerData.setData("entityUuid", FastUUID.toString(entity.getUniqueId()));
                String result = GameStore.getGame(player).run(EventBundle.ATTACK, player, attacker);
                if (result.equals("false")) event.setCancelled(true);
            }
        }
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (!player.isOnline()) return;
            MiniGame game = GameStore.getGame(player);
            GameData playerData = game.getPlayerData(player.getUniqueId());
            playerData.setData("damage", String.valueOf(event.getDamage()));
            playerData.setData("damageCause", event.getCause().name());
            playerData.setData("damageFinal", String.valueOf(event.getFinalDamage()));
            playerData.setData("attackerUuid", FastUUID.toString(attacker.getUniqueId()));
            String result = GameStore.getGame(player).run(EventBundle.DAMAGE, player, attacker);
            if (result.equals("false")) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setItemStack("mainHandItem", event.getMainHandItem());
        playerData.setItemStack("offHandItem", event.getOffHandItem());
        String result = GameStore.getGame(player).run(EventBundle.SWAP_HAND, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setBlock("blockBreak", event.getBlock().getState());
        String result = GameStore.getGame(player).run(EventBundle.BLOCK_BREAK, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setBlock("blockPlace", event.getBlock().getState());
        String result = GameStore.getGame(player).run(EventBundle.BLOCK_PLACE, player);
        if (result.equals("false")) event.setCancelled(true);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        MiniGame game = GameStore.getGame(player);
        GameData playerData = game.getPlayerData(player.getUniqueId());
        playerData.setLocation("respawnLocation", event.getRespawnLocation());
        GameStore.getGame(player).run(EventBundle.PLAYER_RESPAWN, player);
        Location respawnLocation = playerData.getLocation("respawnLocation");
        if (respawnLocation != null) event.setRespawnLocation(respawnLocation);
    }

}
