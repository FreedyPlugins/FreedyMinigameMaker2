package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
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
                String message = FileParser.toColor(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
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
                            GameAlert.CREATED.print(sender, new String[]{message});
                        } catch (Exception e) {
                            GameAlert.INVALID_GAME_NAME.print(sender, new String[]{});
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
                        GameAlert.DELETED.print(sender, new String[]{message});
                        return true;
                    }
                    case "game":
                    case "games": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        Set<String> games = GameStore.getGameNames();
                        if (games.size() == 0) GameAlert.GAME_NOT_EXISTS.print(sender, new String[]{});
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
                        if (sender instanceof Player) GameAlert.RELOADING.print(sender, new String[]{});
                        GameAlert.RELOADING.printLog();
                        if (game == null) {
                            EventBundle.init();
                            GameAlert.init();
                            GameStore.init();
                        } else game.reload();
                        long after = System.currentTimeMillis();
                        String[] strings = {String.valueOf((double) (after - before) / 1000D)};
                        GameAlert.RELOADED.printLog(strings);
                        if (sender instanceof Player) GameAlert.RELOADED.print(sender, strings);
                        return true;
                    }
                    case "save":
                    case "saves": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        long before = System.currentTimeMillis();
                        if (sender instanceof Player) GameAlert.SAVING.print(sender, new String[]{});
                        GameAlert.SAVING.printLog();
                        GameDataStore.save();
                        long after = System.currentTimeMillis();
                        String[] strings = {String.valueOf((double) (after - before) / 1000D)};
                        GameAlert.SAVED.printLog(strings);
                        if (sender instanceof Player) GameAlert.SAVED.print(sender, strings);
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
                final String message = FileParser.toColor(String.join(" ", Arrays.copyOfRange(args, playerInArgs == null ? 1 : 2, args.length)));
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
                    case "do": {
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
                String message = FileParser.toColor(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                if (!(sender instanceof Player)) {
                    GameAlert.ONLY_PLAYER.print();
                    return true;
                }
                Player player = (Player) sender;
                switch (args[0]) {
                    case "run":
                    case "proc":
                    case "process":
                    case "parse": {
                        if (!sender.hasPermission("freedyminigamemaker.admin")) {
                            GameAlert.NEED_PERMISSION.print(sender, new String[]{});
                            return true;
                        }
                        Process process = FileParser.parseProcess(new ParseUnit(), message);
                        process.run(GameStore.getHubGame(), ProcUnit.getNewProcUnit(player));
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            if (sender.isOp()) {
                if (!(e instanceof IllegalArgumentException)) e.printStackTrace();
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
        Bukkit.dispatchCommand(sender, "bukkit:about " + FMGPlugin.getInst().getName());
    }

}
