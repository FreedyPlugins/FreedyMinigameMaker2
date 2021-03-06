package kr.jongwonlee.fmg.proc.data.control;

import com.eatthepath.uuid.FastUUID;
import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

@Processable(alias = {"target"})
public class Target implements Process {

    SmallFrontBrace frontBrace;
    List<Process> processList;
    boolean isOnline;
    boolean isName;
    boolean isList;
    boolean isEntity;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        isName = parseUnit.useExecutor(ProcType.NAME);
        isList = parseUnit.useExecutor(ProcType.LIST);
        isEntity = parseUnit.useExecutor(ProcType.EXECUTE_ENTITY);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
            processList = frontBrace.cutBehindEndBrace();
        } else parseUnit.useExecutor(getType());
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) {
            return "";
        }
        final org.bukkit.entity.Player originPlayer = procUnit.target.player;
        try {
            Process process = processList.get(0);
            String name = process.run(miniGame, procUnit);
            if (isEntity) {
                try {
                    final org.bukkit.entity.Entity originEntity = procUnit.target.entity;
                    procUnit.target.entity = Bukkit.getEntity(FastUUID.parseUUID(name));
                    String value = frontBrace.getLastProc().run(miniGame, procUnit);
                    procUnit.target.entity = originEntity;
                    return value;
                } catch (Exception ignored) { }
            } else if (isList) {
                List<String> list = null;
                if (process.getType() == ProcType.EXECUTE_GAME) list = miniGame.getGameData().getList(name);
                else if (process.getType() == ProcType.EXECUTE_ONLINE) list = GameDataStore.getInst().getList(name);
                else if (originPlayer != null) list = GameStore.getPlayerData(originPlayer.getUniqueId()).getList(name);
                Process proc2 = processList.get(2);
                String value2 = proc2.run(miniGame, procUnit);
                Process lastProc = frontBrace.getLastProc();
                if (proc2.getType() == ProcType.EXECUTE_GAME) {
                    if (list != null) new ArrayList<>(list).forEach(e -> {
                        miniGame.getGameData().setData(value2, e);
                        lastProc.run(miniGame, procUnit);
                    });
                }
                else if (proc2.getType() == ProcType.EXECUTE_ONLINE) {
                    if (list != null) {
                        for (String e : new ArrayList<>(list)) {
                            GameDataStore.getInst().setData(value2, e);
                            lastProc.run(miniGame, procUnit);
                            String returned = procUnit.getReturned();
                            if (returned != null) return returned;
                        }
                    }
                }
                else if (originPlayer != null) {
                    GameData playerData = GameStore.getPlayerData(originPlayer.getUniqueId());
                    if (list != null) {
                        for (String e : new ArrayList<>(list)) {
                            playerData.setData(value2, e);
                            lastProc.run(miniGame, procUnit);
                            String returned = procUnit.getReturned();
                            if (returned != null) return returned;
                        }
                    }
                }
                //get list target ( all game name | all element )
            } else if (isOnline) {
                List<String> list = null;
                if (process.getType() == ProcType.EXECUTE_GAME) list = miniGame.getGameData().getList(name);
                else if (process.getType() == ProcType.EXECUTE_ONLINE) list = GameDataStore.getInst().getList(name);
                else if (originPlayer != null) list = GameStore.getPlayerData(originPlayer.getUniqueId()).getList(name);
                Process lastProc = frontBrace.getLastProc();
                if (list != null) new ArrayList<>(list).forEach(element -> {
                    try {
                        if (isName) procUnit.target.player = Bukkit.getPlayer(element);
                        else procUnit.target.player = Bukkit.getPlayer(FastUUID.parseUUID(element));
                        lastProc.run(miniGame, procUnit);
                    } catch (Exception ignored) { }
                });
                procUnit.target.player = originPlayer;
                return "";
            } else {
                try {
                    if (isName) procUnit.target.player = Bukkit.getPlayer(name);
                    else procUnit.target.player = Bukkit.getPlayer(FastUUID.parseUUID(name));
                    String value = frontBrace.getLastProc().run(miniGame, procUnit);
                    procUnit.target.player = originPlayer;
                    return value;
                } catch (Exception ignored) { }
            }
        } catch (Exception e) {
            procUnit.target.player = originPlayer;
            return "";
        }
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.TARGET;
    }
}
