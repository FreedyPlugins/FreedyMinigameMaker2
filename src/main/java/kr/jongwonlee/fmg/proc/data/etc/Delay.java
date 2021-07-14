package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.FMGPlugin;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;

import java.util.List;

@Processable(alias = {"delay"})
public class Delay implements Process {

    Process process;
    SmallFrontBrace frontBrace;
    boolean isAsync;

    @Override
    public ProcType getType() {
        return ProcType.DELAY;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
        isAsync = parseUnit.useExecutor(ProcType.EXECUTE_ASYNC);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
        List<Process> processList = frontBrace.getProcessList();
        if (processList.size() == 0) return;
        Process process = processList.get(processList.size() - 1);
        if (process.getType() == ProcType.SMALL_END_BRACE) {
            processList.add(FileParser.getOneMoreLine(parseUnit, ""));
        }

    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        List<Process> processList = frontBrace.getProcessList();
        String delay = processList.get(0).run(miniGame, procUnit);
        Runnable runnable = () -> processList.get(processList.size() - 1).run(miniGame, procUnit);
        if (isAsync) FMGPlugin.runTaskLaterAsync(runnable, ((long) Double.parseDouble(delay)));
        else FMGPlugin.runTaskLater(runnable, ((long) Double.parseDouble(delay)));
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}
