package kr.jongwonlee.fmg.image;

import kr.jongwonlee.fmg.FMGPlugin;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.image.drawable.ImageText;
import kr.jongwonlee.fmg.nms.NMS;
import kr.jongwonlee.fmg.proc.FileReader;
import kr.jongwonlee.fmg.util.FileStore;
import kr.jongwonlee.fmg.util.GameAlert;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ImageEditor implements Listener {

    private static Map<UUID, ImageEditor> imageMap;

    private Player player;
    private String name;
    private Image image;
    private int line;
    private List<String> source;

    public static void init() {
        imageMap = new HashMap<>();
    }

    public static void openEditor(Player player, String name) {
        FMGPlugin.runTaskAsync(() -> {
            ImageEditor imageEditor = new ImageEditor();
            imageEditor.name = name;
            imageEditor.image = Image.getNewImage(player);
            if (imageMap.containsKey(player.getUniqueId())) {
                ImageEditor imageEditor1 = imageMap.get(player.getUniqueId());
                Image image = imageEditor1.image;
                NMS.getImageViewer().destroyMap(image);
                imageMap.remove(player.getUniqueId());
                FMGPlugin.unregisterEvent(imageEditor1);
            }
            FMGPlugin.registerEvent(imageEditor);
            try {
                FileReader fileReader = new FileReader(name);
                imageEditor.source = new ArrayList<>();
                imageEditor.player = player;
                String line;
                while ((line = fileReader.readLine()) != null) {
                    imageEditor.source.add(line);
                }
                if (imageEditor.source.isEmpty()) imageEditor.source.add("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageMap.put(player.getUniqueId(), imageEditor);
            ImageText imageText = new ImageText();
            imageText.setPos(new Vector2D(5, 15));
            imageEditor.image.drawableMap.put("current", imageText);
            imageText.setString("§bFreedyMinigameMaker\n§bEditor\n§aType 'save' to save!\n§aType 'exit' to exit!\n§aType anything to insert!\n§dScroll mouse to start...");
            imageText.setFont(new Font("MineCrafter", Font.PLAIN, 40));
            imageEditor.image.update();
            imageText.setFont(new Font("MineCrafter", Font.PLAIN, 25));
        });
    }

    public void refresh() {
        StringBuilder builder = new StringBuilder();
        List<String> source = this.source;
        if (line >= 9) {
            source = source.subList(line - 9, source.size());
        }//line - line - 9
        int line = Math.min(this.line, 9);
        for (int i = 0; i < source.size(); i++) {
            if (line == i) builder.append("§a");
            else builder.append("§e");
            builder.append(source.get(i));
            builder.append("\n");
        }
        ImageText imageText = (ImageText) image.drawableMap.get("current");
        imageText.setString(builder.toString());
        image.update();
    }

    public void save() {
        FileStore fileStore = new FileStore(name + Settings.getExtension(), true);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileStore.getFile()));
            for (String line : source) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            GameAlert.ERROR.print(new String[]{"File " + name + " not found."});
        }
    }

    @EventHandler
    public void onTabComplete(PlayerChatTabCompleteEvent event) {
        if (!event.getPlayer().equals(player)) return;
        Collection<String> tabCompletions = event.getTabCompletions();
        tabCompletions.clear();
        tabCompletions.add(source.get(line));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        FMGPlugin.runTaskAsync(() -> {
            Player player = event.getPlayer();
            if (imageMap.containsKey(player.getUniqueId())) {
                ImageEditor imageEditor = imageMap.get(player.getUniqueId());
                Image image = imageEditor.image;
                NMS.getImageViewer().destroyMap(image);
                imageMap.remove(player.getUniqueId());
                FMGPlugin.unregisterEvent(this);
            }
        });
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().equals(player)) return;
        if (!image.isSettled) return;
        event.setCancelled(true);
        FMGPlugin.runTaskAsync(() -> {
            String message = event.getMessage();
            if (message.equalsIgnoreCase("save")) {
                save();
                player.sendMessage("Saved!");
            } else if (message.equalsIgnoreCase("del")) {
                source.remove(line);
            } else if (message.equalsIgnoreCase("exit")) {
                FMGPlugin.runTaskAsync(() -> {
                    Player player = event.getPlayer();
                    if (imageMap.containsKey(player.getUniqueId())) {
                        ImageEditor imageEditor = imageMap.get(player.getUniqueId());
                        Image image = imageEditor.image;
                        NMS.getImageViewer().destroyMap(image);
                        imageMap.remove(player.getUniqueId());
                        FMGPlugin.unregisterEvent(this);
                    }
                });
                return;
            } else source.set(line, message);
            refresh();
        });
    }

        @EventHandler
    public void onScroll(PlayerItemHeldEvent event) {
        if (!event.getPlayer().equals(player)) return;
        FMGPlugin.runTaskAsync(() -> {
            if (!image.isSettled) return;
            int newSlot = event.getNewSlot();
            int previousSlot = event.getPreviousSlot();
            boolean boolResult;
            if (newSlot == 8&& previousSlot == 0) boolResult = false;
            else if (newSlot == 0 && previousSlot == 8) boolResult = true;
            else boolResult = newSlot > previousSlot;
            if (!boolResult) {
                if (line != 0) {
                    if (source.get(source.size() - 1).equals("")) source.remove(source.size() - 1);
                    line--;
                    refresh();
                }
            } else if (line < source.size()) {
                line++;
                if (!source.get(source.size() - 1).equals("") && line >= source.size()) source.add("");
                refresh();
            }
        });
    }

}
