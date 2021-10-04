package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.entity.Player;

import java.util.List;

@Processable(alias = {"fly", "flying"})
public class Flying implements Process {

    Process process;
    SmallFrontBrace frontBrace;
    List<Process> processList;
    boolean isSet;
    boolean isGet;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isGet = parseUnit.useExecutor(ProcType.EXECUTE_GET);
        process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
            processList = frontBrace.cutBehindEndBrace();
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        Player player = procUnit.target.player;
        try {
            if (isSet) {
                Process proc1 = processList.get(0);
                String value1 = proc1.run(miniGame, procUnit);
                    if (isGet) player.setAllowFlight(value1.equals("true"));
                    else player.setFlying(value1.equals("true"));
            } else {
                if (isGet) return player.getAllowFlight() ? "true" : "false";
                else return player.isFlying() ? "true" : "false";
            }
            return frontBrace.getLastProc().run(miniGame, procUnit);
        } catch (Exception ignored) {
            return process.run(miniGame, procUnit);
        }
    }

    @Override
    public ProcType getType() {
        return ProcType.FLYING;
    }
}
