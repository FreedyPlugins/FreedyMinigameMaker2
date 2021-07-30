package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

@Processable(alias = "velocity")
public class Velocity implements Process {

    Process process;
    List<Process> processList;
    SmallFrontBrace frontBrace;
    boolean isSet;
    boolean isAdd;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
            processList = frontBrace.cutBehindEndBrace();
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) return process.run(miniGame, procUnit);
        Player player = procUnit.target.player;
        try {
            Process proc1 = processList.get(0);
            String value1 = proc1.run(miniGame, procUnit);
            Process proc2 = processList.get(2);
            String value2 = proc2.run(miniGame, procUnit);
            Process proc3 = processList.get(4);
            String value3 = proc3.run(miniGame, procUnit);
            Vector vector = new Vector(Double.parseDouble(value1), Double.parseDouble(value2), Double.parseDouble(value3));
            if (isSet) player.setVelocity(vector);
            else if (isAdd) player.setVelocity(player.getVelocity().add(vector));
        } catch (Exception ignored) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.VELOCITY;
    }
}
