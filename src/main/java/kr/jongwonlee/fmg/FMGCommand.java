package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.process.EventBundle;
import kr.jongwonlee.fmg.setting.DataStore;
import kr.jongwonlee.fmg.setting.ItemStore;
import kr.jongwonlee.fmg.setting.LocationStore;
import kr.jongwonlee.fmg.setting.Settings;
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
                switch (args[0]) {
                    case "create": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) return true;
                        if (message.length() == 0) return false;
                        GameStore.createGame(message);
                        return true;
                    }
                    case "delete": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) return true;
                        if (message.length() == 0) return false;
                        GameStore.removeShop(message);
                        return true;
                    }
                    case "games":
                    case "list": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) return true;
                        Set<String> games = GameStore.getGameNames();
                        if (games.size() == 0) sender.sendMessage("Game not exist");
                        else games.forEach(sender::sendMessage);
                        return true;
                    }
                    case "rl":
                    case "rel":
                    case "relo":
                    case "reloa":
                    case "reload": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) return true;
                        long before = System.currentTimeMillis();
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
                        if (!sender.hasPermission("freedyminigamemaker.admin")) return true;
                        about(sender);
                        return true;
                    }
                }
            }
            { // console or player with args option ...
                final Player playerInArgs = Bukkit.getPlayer(args[1]);
                final String message = String.join(" ", Arrays.copyOfRange(args, playerInArgs == null ? 1 : 2, args.length));
                final Player player = playerInArgs == null ? ((Player) sender) : playerInArgs;
                MiniGame game = GameStore.getGame(message);
                switch (args[0]) {
                    case "join": {
                        if (!sender.hasPermission("freedyminigamemaker.join." + message)) return true;
                        game.join(player.getUniqueId());
                        return true;
                    }
                    case "left":
                    case "quit": {
                        if (!sender.hasPermission("freedyminigamemaker.quit." + message)) return true;
                        GameStore.getGames().forEach(miniGame -> miniGame.quit(player.getUniqueId()));
                        return true;
                    }
                    case "do":
                    case "run":
                    case "proc":
                    case "process": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) return true;
                        GameStore.getGame(Settings.getHubGameName()).run(message.toLowerCase(), player);
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
                        ItemStore.setItemStack(message, player.getInventory().getItemInMainHand());
                        return true;
                    }
                    case "loc":
                    case "locs":
                    case "location": {
                        LocationStore.setLocation(message, player.getLocation());
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            if (sender.isOp()) {
                e.printStackTrace();
                about(sender);
                return false;
            } else return true;
        }
        return true;
    }

    private static void about(CommandSender sender) {
        Bukkit.dispatchCommand(sender, "about " + FMGPlugin.getInst().getName());
    }
}
