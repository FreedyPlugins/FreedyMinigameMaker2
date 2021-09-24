package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.List;

@Processable(alias = "bossbar")
public class BossBar implements Process {

    Process process;
    SmallFrontBrace frontBrace;
    List<Process> processList;
    boolean isGame;
    boolean isCreate;
    boolean isOpen;
    boolean isClose;
    boolean isRemove;
    boolean isSet;
    boolean isType;
    boolean isColor;
    boolean isSize;
    boolean isExists;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isCreate = parseUnit.useExecutor(ProcType.EXECUTE_CREATE);
        isOpen = parseUnit.useExecutor(ProcType.EXECUTE_OPEN);
        isClose = parseUnit.useExecutor(ProcType.EXECUTE_CLOSE);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isType = parseUnit.useExecutor(ProcType.EXECUTE_TYPE);
        isColor = parseUnit.useExecutor(ProcType.EXECUTE_COLOR);
        isSize = parseUnit.useExecutor(ProcType.EXECUTE_SIZE);
        isExists = parseUnit.useExecutor(ProcType.EXECUTE_EXISTS);
        process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
            processList = frontBrace.cutBehindEndBrace();
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace == null) return process.run(miniGame, procUnit);
            Player player = procUnit.target.player;
            Process proc1 = processList.get(0);
            String name = proc1.run(miniGame, procUnit);
            if (isExists) {
                org.bukkit.boss.BossBar bossBar;
                if (isGame) bossBar = miniGame.getGameData().getBossBar(name);
                else bossBar = GameDataStore.getPlayerData(player.getUniqueId()).getBossBar(name);
                return ( bossBar != null ? "true" : "false" ) + frontBrace.getLastProc().run(miniGame, procUnit);
            }
            else if (isRemove) {
                org.bukkit.boss.BossBar bossBar;
                if (isGame) {
                    bossBar = miniGame.getGameData().getBossBar(name);
                    miniGame.getGameData().setBossBar(name, null);
                } else {
                    bossBar = GameDataStore.getPlayerData(player.getUniqueId()).getBossBar(name);
                    GameDataStore.getPlayerData(player.getUniqueId()).setBossBar(name, null);
                }
                bossBar.removeAll();
            } else if (isClose) {
                org.bukkit.boss.BossBar bossBar;
                if (isGame) bossBar = miniGame.getGameData().getBossBar(name);
                else bossBar = GameDataStore.getPlayerData(player.getUniqueId()).getBossBar(name);
                bossBar.removePlayer(player);
            } else if (isOpen) {
                org.bukkit.boss.BossBar bossBar;
                if (isGame) bossBar = miniGame.getGameData().getBossBar(name);
                else bossBar = GameDataStore.getPlayerData(player.getUniqueId()).getBossBar(name);
                bossBar.addPlayer(player);
            } else if (isCreate) {
                {
                    org.bukkit.boss.BossBar bossBar;
                    if (isGame) {
                        bossBar = miniGame.getGameData().getBossBar(name);
                        miniGame.getGameData().setBossBar(name, null);
                    } else {
                        bossBar = GameDataStore.getPlayerData(player.getUniqueId()).getBossBar(name);
                        GameDataStore.getPlayerData(player.getUniqueId()).setBossBar(name, null);
                    }
                    if (bossBar != null) bossBar.removeAll();
                }
                Process proc2 = processList.get(2);
                String value2 = proc2.run(miniGame, procUnit);
                BarStyle barStyle;
                try {
                    Process proc3 = processList.get(4);
                    String value3 = proc3.run(miniGame, procUnit);
                    switch (value3) {
                        case "6":
                            barStyle = BarStyle.SEGMENTED_6;
                            break;
                        case "10":
                            barStyle = BarStyle.SEGMENTED_10;
                            break;
                        case "12":
                            barStyle = BarStyle.SEGMENTED_12;
                            break;
                        case "20":
                            barStyle = BarStyle.SEGMENTED_20;
                            break;
                        default:
                            barStyle = BarStyle.SOLID;
                    }
                } catch (Exception ignored) {
                    barStyle = BarStyle.SOLID;
                }
                org.bukkit.boss.BossBar bossBar = Bukkit.createBossBar("", BarColor.valueOf(value2), barStyle);
                if (isGame) miniGame.getGameData().setBossBar(name, bossBar);
                else GameDataStore.getPlayerData(player.getUniqueId()).setBossBar(name, bossBar);
            } else if (isSet) {
                Process proc2 = processList.get(2);
                String value2 = proc2.run(miniGame, procUnit);
                org.bukkit.boss.BossBar bossBar;
                if (isGame) bossBar = miniGame.getGameData().getBossBar(name);
                else bossBar = GameDataStore.getPlayerData(player.getUniqueId()).getBossBar(name);
                if (isColor) bossBar.setColor(BarColor.valueOf(value2));
                else if (isType) {
                    BarStyle barStyle;
                    switch (value2) {
                        case "6":
                            barStyle = BarStyle.SEGMENTED_6;
                            break;
                        case "10":
                            barStyle = BarStyle.SEGMENTED_10;
                            break;
                        case "12":
                            barStyle = BarStyle.SEGMENTED_12;
                            break;
                        case "20":
                            barStyle = BarStyle.SEGMENTED_20;
                            break;
                        default:
                            barStyle = BarStyle.SOLID;
                    }
                    bossBar.setStyle(barStyle);
                } else if (isSize) bossBar.setProgress(Double.parseDouble(value2));
                else bossBar.setTitle(value2);
            }
            return frontBrace.getLastProc().run(miniGame, procUnit);
        } catch (Exception ignored) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
    }

    @Override
    public ProcType getType() {
        return ProcType.BOSS_BAR;
    }
}
