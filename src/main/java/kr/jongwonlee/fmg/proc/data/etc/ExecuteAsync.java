package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.FMGPlugin;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = {"async"})
public class ExecuteAsync implements Process {

    Process process;
    ProcType procType;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        parseUnit.setAsync(true);
        process = FileParser.parseProcess(parseUnit, arguments);
        procType = process.getType();
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (procType == ProcType.DELAY) {
            return process.run(miniGame, procUnit);
        }
        FMGPlugin.runTaskAsync(() -> process.run(miniGame, procUnit));
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE_ASYNC;
    }
}
