package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.conf.DataStore;
import kr.jongwonlee.fmg.conf.ItemStore;
import kr.jongwonlee.fmg.conf.LocationStore;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.image.ImageEditor;
import kr.jongwonlee.fmg.proc.EventBundle;
import kr.jongwonlee.fmg.proc.ProcTarget;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.util.GameAlert;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;

public class FMGCommand implements CommandExecutor {

    public static void init() {
        FMGPlugin.getInst().getCommand("fmg").setExecutor(new FMGCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            { //console or player ...
                String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                MiniGame game = GameStore.getGame(message);
                switch (args[0].toLowerCase()) {
                    case "create": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        if (message.length() == 0) return false;
                        try {
                            GameStore.createGame(message);
                        } catch (Exception e) {
                            sender.sendMessage("Invalid game name");
                        }
                        return true;
                    }
                    case "delete": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        if (message.length() == 0) return false;
                        GameStore.removeGame(message);
                        return true;
                    }
                    case "games":
                    case "list": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        Set<String> games = GameStore.getGameNames();
                        if (games.size() == 0) sender.sendMessage("Game not exist");
                        else games.forEach(sender::sendMessage);
                        return true;
                    }
                    case "r":
                    case "rl":
                    case "rel":
                    case "relo":
                    case "reloa":
                    case "reload": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        long before = System.currentTimeMillis();
                        if (sender instanceof Player) sender.sendMessage("Reloading...");
                        FMGPlugin.getInst().getLogger().info("Reloading...");
                        if (game == null) {
                            ItemStore.init();
                            LocationStore.init();
                            DataStore.init();
                            EventBundle.init();
                            GameAlert.init();
                            GameStore.init();
                            GameStore.getGames().forEach(MiniGame::reload);
                        }
                        else game.reload();
                        long after = System.currentTimeMillis();
                        String alert = String.format("Reloaded (%ss)!", ((double) (after - before)) / 1000D);
                        FMGPlugin.getInst().getLogger().info(alert);
                        if (sender instanceof Player) sender.sendMessage(alert);
                        return true;
                    }
                    case "info":
                    case "about":
                    case "help": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        about(sender);
                        return true;
                    }
                }
            }
            { // console or player with args option ...
                final Player playerInArgs = args.length > 1 ? Bukkit.getPlayer(args[1]) : null;
                final String message = String.join(" ", Arrays.copyOfRange(args, playerInArgs == null ? 1 : 2, args.length));
                final Player player = playerInArgs == null ? sender instanceof Player ? ((Player) sender) : null : playerInArgs;
                MiniGame game = GameStore.getGame(message);
                switch (args[0]) {
                    case "join": {
                        if (!sender.hasPermission("freedyminigamemaker.join." + message)) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        if (player == null) GameAlert.ONLY_PLAYER.print();
                        else game.join(player.getUniqueId());
                        return true;
                    }
                    case "left":
                    case "quit": {
                        if (!sender.hasPermission("freedyminigamemaker.quit." + message)) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        if (player == null) GameAlert.ONLY_PLAYER.print();
                        else GameStore.getGames().forEach(miniGame -> {
                            if (!miniGame.equals(GameStore.getHubGame())) miniGame.quit(player.getUniqueId());
                        });
                        return true;
                    }
                    case "do":
                    case "run":
                    case "proc":
                    case "process": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        if (sender instanceof Player) GameStore.getGame(Settings.getHubGameName()).run(message.toLowerCase(), player);
                        else GameStore.getGame(Settings.getHubGameName()).run(message.toLowerCase(), new ProcUnit(new ProcTarget()));
                        return true;
                    }
                }
            }
            { //only player
                String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                if (!(sender instanceof Player)) {
                    GameAlert.ONLY_PLAYER.print();
                    return true;
                }
                Player player = (Player) sender;
                switch (args[0]) {
                    case "item":
                    case "items": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        ItemStore.setItemStack(message, player.getInventory().getItemInMainHand());
                        return true;
                    }
                    case "loc":
                    case "locs":
                    case "location": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        LocationStore.setLocation(message, player.getLocation());
                        return true;
                    }
                    case "edit":
                    case "editor": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        ImageEditor.openEditor(player, message);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            if (sender.isOp()) {
                e.printStackTrace();
                about(sender);
                return false;
            } else {
                GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                return true;
            }
        }
        return true;
    }

    private static void about(CommandSender sender) {
        Bukkit.dispatchCommand(sender, "about " + FMGPlugin.getInst().getName());
    }
}
