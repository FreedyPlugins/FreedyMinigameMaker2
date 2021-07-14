package kr.jongwonlee.fmg.proc.data.control;

import com.eatthepath.uuid.FastUUID;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
import org.bukkit.Bukkit;

import java.util.List;

@Processable(alias = {"target"})
public class Target implements Process {

    SmallFrontBrace frontBrace;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
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
        org.bukkit.entity.Player originPlayer = procUnit.target.player;
        try {
            List<Process> processList = frontBrace.getProcessList();
            String name = processList.get(0).run(miniGame, procUnit);
            try {
                procUnit.target.player = Bukkit.getPlayer(FastUUID.parseUUID(name));
            } catch (Exception ignored) {
                org.bukkit.entity.Player player = Bukkit.getPlayer(name);
                if (player != null) procUnit.target.player = player;
            }
            procUnit.target.player = originPlayer;
            return processList.get(processList.size() - 1).run(miniGame, procUnit);
        } catch (Exception e) {
            procUnit.target.player = originPlayer;
            return "";
        }
    }

    @Override
    public ProcType getType() {
        return ProcType.TARGET;
    }
}
