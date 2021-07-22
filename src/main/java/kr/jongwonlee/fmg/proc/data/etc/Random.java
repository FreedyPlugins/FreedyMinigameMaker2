package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import net.jafama.FastMath;

import java.util.List;

@Processable(alias = {"random", "rand"})
public class Random implements Process {

    private Process process;
    private SmallFrontBrace frontBrace;

    @Override
    public ProcType getType() {
        return ProcType.RANDOM;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        boolean isGet = parseUnit.useExecutor(ProcType.EXECUTE_GET);
        if (isGet) parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace == null) return process.run(miniGame, procUnit);
            List<Process> processList = frontBrace.getProcessList();
            if (procUnit.target.player != null) {
                String min = processList.get(0).run(miniGame, procUnit);
                String max = processList.get(2).run(miniGame, procUnit);
                double result = FastMath.random() * Double.parseDouble(max) + Double.parseDouble(min);
                return String.valueOf(result);
            }
        } catch (NumberFormatException e) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}
