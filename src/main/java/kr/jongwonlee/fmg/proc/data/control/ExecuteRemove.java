package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = {"remove", "delete", "del"})
public class ExecuteRemove implements Process {

    Process process;

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE_REMOVE;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return process.run(miniGame, procUnit);
    }

}
