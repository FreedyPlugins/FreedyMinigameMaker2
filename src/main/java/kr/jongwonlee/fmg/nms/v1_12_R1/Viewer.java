package kr.jongwonlee.fmg.nms.v1_12_R1;

import kr.jongwonlee.fmg.image.Image;
import kr.jongwonlee.fmg.nms.ImageViewer;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import static java.util.Collections.emptyList;

public class Viewer implements ImageViewer {

    public void createMap(Image image) {
        WorldServer worldServer = ((CraftWorld) image.location.getWorld()).getHandle();
        int width = image.bufferedImage.getWidth() / 128;
        int height = image.bufferedImage.getHeight() / 128;
        if (image.subImages == null) image.subImages = new ImageFrame[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ImageFrame imageFrame = image.subImages[x][y];
                if (imageFrame == null) {
                    imageFrame = new ImageFrame();
                    image.subImages[x][y] = imageFrame;
                    int mapId = Bukkit.createMap(image.location.getWorld()).getId();
                    EntityItemFrame frame = new EntityItemFrame(worldServer);
                    frame.setInvisible(false);
                    Location loc = addLocation(image.face, image.location, width - x, height - y);
                    frame.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
                    frame.setDirection(CraftBlock.blockFaceToNotch(image.face));
                    net.minecraft.server.v1_12_R1.ItemStack item = new net.minecraft.server.v1_12_R1.ItemStack(Items.FILLED_MAP);
                    item.setData(mapId);
                    frame.setItem(item);
                    imageFrame.setFrame(frame);
                }
                imageFrame.setPixels(createPixels(deepCopy(image.bufferedImage).getSubimage(x * 128, y * 128, 128, 128)));
            }
        }
    }

    @Deprecated
    public void applyMapToAllPlayer(Image image) {
        if (image.subImages == null || !image.isSettled) return;
        final int width = image.bufferedImage.getWidth() / 128;
        final int height = image.bufferedImage.getHeight() / 128;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!image.isSettled) continue;
                ImageFrame imageFrame = image.subImages[x][y];
                if (imageFrame == null) continue;
                EntityItemFrame frame = imageFrame.getFrame();
                PacketPlayOutSpawnEntity spawnEntityPacket = new PacketPlayOutSpawnEntity(frame, 71, frame.direction.get2DRotationValue(), frame.getBlockPosition());
                PacketPlayOutEntityMetadata entityMetadataPacket = new PacketPlayOutEntityMetadata(frame.getId(), frame.getDataWatcher(), true);
                int mapId = frame.getItem().getData();
                PacketPlayOutMap mapPacket = new PacketPlayOutMap(mapId, (byte) 3, false, emptyList(), imageFrame.getPixels(), 0, 0, 128, 128);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (!image.location.getWorld().equals(player.getWorld())) return;
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                    connection.sendPacket(spawnEntityPacket);
                    connection.sendPacket(entityMetadataPacket);
                    connection.sendPacket(mapPacket);
                });
            }
        }
    }

    public void applyMap(Image image) {
        if (image.subImages == null || !image.isSettled) return;
        if (!image.location.getWorld().equals(image.player.getWorld())) return;
        final int width = image.bufferedImage.getWidth() / 128;
        final int height = image.bufferedImage.getHeight() / 128;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!image.isSettled) continue;
                ImageFrame imageFrame = image.subImages[x][y];
                if (imageFrame == null) continue;
                EntityItemFrame frame = imageFrame.getFrame();
                PacketPlayOutSpawnEntity spawnEntityPacket = new PacketPlayOutSpawnEntity(frame, 71, frame.direction.get2DRotationValue(), frame.getBlockPosition());
                PacketPlayOutEntityMetadata entityMetadataPacket = new PacketPlayOutEntityMetadata(frame.getId(), frame.getDataWatcher(), true);
                int mapId = frame.getItem().getData();
                PacketPlayOutMap mapPacket = new PacketPlayOutMap(mapId, (byte) 3, false, emptyList(), imageFrame.getPixels(), 0, 0, 128, 128);
                PlayerConnection connection = ((CraftPlayer) image.player).getHandle().playerConnection;
                connection.sendPacket(spawnEntityPacket);
                connection.sendPacket(entityMetadataPacket);
                connection.sendPacket(mapPacket);
            }
        }
    }

    public void destroyMap(Image image) {
        if (image.subImages == null || !image.isSettled) return;
        ImageFrame[][] finalSubImages = image.subImages;
        Bukkit.getOnlinePlayers().forEach(player -> {
            final int width = image.bufferedImage.getWidth() / 128;
            final int height = image.bufferedImage.getHeight() / 128;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    ImageFrame imageFrame = finalSubImages[x][y];
                    if (imageFrame == null) continue;
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                    connection.sendPacket(new PacketPlayOutEntityDestroy(imageFrame.getFrame().getId()));
                }
            }
        });
        image.subImages = null;
    }



}