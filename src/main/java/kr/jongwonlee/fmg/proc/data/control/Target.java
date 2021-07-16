package kr.jongwonlee.fmg.proc.data.control;

import com.eatthepath.uuid.FastUUID;
import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

@Processable(alias = {"target"})
public class Target implements Process {

    SmallFrontBrace frontBrace;
    boolean isOnline;
    boolean isName;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        isName = parseUnit.useExecutor(ProcType.NAME);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
        } else parseUnit.useExecutor(getType());
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) {
            return "";
        }
        final org.bukkit.entity.Player originPlayer = procUnit.target.player;
        try {
            List<Process> processList = frontBrace.getProcessList();
            Process process = processList.get(0);
            String name = process.run(miniGame, procUnit);
            if (isOnline) {
                List<String> list = null;
                if (process.getType() == ProcType.EXECUTE_GAME) list = miniGame.getGameData().getList(name);
                else if (process.getType() == ProcType.EXECUTE_ONLINE) list = GameDataStore.getInst().getList(name);
                else if (originPlayer != null) list = miniGame.getPlayerData(originPlayer.getUniqueId()).getList(name);
                if (list != null) new ArrayList<>(list).forEach(element -> {
                    try {
                        if (isName) procUnit.target.player = Bukkit.getPlayer(element);
                        procUnit.target.player = Bukkit.getPlayer(FastUUID.parseUUID(element));
                        frontBrace.getLastProc().run(miniGame, procUnit);
                    } catch (Exception ignored) { }
                });
                procUnit.target.player = originPlayer;
                return "";
            } else {
                try {
                    procUnit.target.player = Bukkit.getPlayer(FastUUID.parseUUID(name));
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
