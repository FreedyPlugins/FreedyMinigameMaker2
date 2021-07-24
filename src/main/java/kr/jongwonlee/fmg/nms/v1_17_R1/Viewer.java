package kr.jongwonlee.fmg.nms.v1_17_R1;

import kr.jongwonlee.fmg.image.Image;
import kr.jongwonlee.fmg.nms.ImageViewer;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMap;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.WorldMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;

import static java.util.Collections.emptyList;

public class Viewer extends ImageViewer {

    public kr.jongwonlee.fmg.nms.v1_17_R1.ImageFrame[][] subImages;


    public void createMap(Image image) {
        WorldServer worldServer = ((CraftWorld) image.location.getWorld()).getHandle();
        int width = image.bufferedImage.getWidth() / 128;
        int height = image.bufferedImage.getHeight() / 128;
        if (subImages == null) subImages = new kr.jongwonlee.fmg.nms.v1_17_R1.ImageFrame[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                kr.jongwonlee.fmg.nms.v1_17_R1.ImageFrame imageFrame = subImages[x][y];
                if (imageFrame == null) {
                    imageFrame = new kr.jongwonlee.fmg.nms.v1_17_R1.ImageFrame();
                    subImages[x][y] = imageFrame;
                    int mapId = Bukkit.createMap(image.location.getWorld()).getId();
                    EntityItemFrame frame = new EntityItemFrame(EntityTypes.R, worldServer);
                    frame.setInvisible(false);
                    Location loc = addLocation(image.face, image.location, width - x, height - y);
                    frame.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
                    frame.setDirection(CraftBlock.blockFaceToNotch(image.face));
                    ItemStack item = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.MAP));
                    item.getOrCreateTag().setInt("map", mapId);
                    frame.setItem(item);
                    imageFrame.setFrame(frame);
                }
                imageFrame.setPixels(createPixels(deepCopy(image.bufferedImage).getSubimage(x * 128, y * 128, 128, 128)));
            }
        }
    }

    @Deprecated
    public void applyMapToAllPlayer(Image image) {
        if (subImages == null || !image.isSettled) return;
        final int width = image.bufferedImage.getWidth() / 128;
        final int height = image.bufferedImage.getHeight() / 128;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!image.isSettled) continue;
                kr.jongwonlee.fmg.nms.v1_17_R1.ImageFrame imageFrame = subImages[x][y];
                if (imageFrame == null) continue;
                EntityItemFrame frame = imageFrame.getFrame();
                PacketPlayOutSpawnEntity spawnEntityPacket = new PacketPlayOutSpawnEntity(frame, EntityTypes.R, frame.getDirection().get2DRotationValue(), frame.getBlockPosition());
                PacketPlayOutEntityMetadata entityMetadataPacket = new PacketPlayOutEntityMetadata(frame.getId(), frame.getDataWatcher(), true);
                int mapId = frame.getItem().getTag().getInt("map");
                PacketPlayOutMap mapPacket = new PacketPlayOutMap(mapId, (byte) 3, false, emptyList(), new WorldMap.b(0, 0, 128, 128, imageFrame.getPixels()));
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (!image.location.getWorld().equals(player.getWorld())) return;
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                    connection.sendPacket(spawnEntityPacket);
                    connection.sendPacket(entityMetadataPacket);
                    connection.sendPacket(mapPacket);
                });
            }
        }
    }

    public void applyMap(Image image) {
        if (subImages == null || !image.isSettled) return;
        if (!image.location.getWorld().equals(image.player.getWorld())) return;
        final int width = image.bufferedImage.getWidth() / 128;
        final int height = image.bufferedImage.getHeight() / 128;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!image.isSettled) continue;
                kr.jongwonlee.fmg.nms.v1_17_R1.ImageFrame imageFrame = subImages[x][y];
                if (imageFrame == null) continue;
                EntityItemFrame frame = imageFrame.getFrame();
                PacketPlayOutSpawnEntity spawnEntityPacket = new PacketPlayOutSpawnEntity(frame, EntityTypes.R, frame.getDirection().get2DRotationValue(), frame.getBlockPosition());
                PacketPlayOutEntityMetadata entityMetadataPacket = new PacketPlayOutEntityMetadata(frame.getId(), frame.getDataWatcher(), true);
                int mapId = frame.getItem().getTag().getInt("map");
                PacketPlayOutMap mapPacket = new PacketPlayOutMap(mapId, (byte) 3, false, emptyList(), new WorldMap.b(0, 0, 128, 128, imageFrame.getPixels()));
                Bukkit.getOnlinePlayers().forEach(player -> {
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                    connection.sendPacket(spawnEntityPacket);
                    connection.sendPacket(entityMetadataPacket);
                    connection.sendPacket(mapPacket);
                });
            }
        }
    }

    public void destroyMap(Image image) {
        if (subImages == null || !image.isSettled) return;
        kr.jongwonlee.fmg.nms.v1_17_R1.ImageFrame[][] finalSubImages = subImages;
        Bukkit.getOnlinePlayers().forEach(player -> {
            final int width = image.bufferedImage.getWidth() / 128;
            final int height = image.bufferedImage.getHeight() / 128;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    ImageFrame imageFrame = finalSubImages[x][y];
                    if (imageFrame == null) continue;
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                    connection.sendPacket(new PacketPlayOutEntityDestroy(imageFrame.getFrame().getId()));
                }
            }
        });
        subImages = null;
    }



}