package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;

@Processable(alias = {"add"})
public class ExecuteAdd implements Process {

    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE_ADD;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        procUnit.setAdd(true);
        process.run(miniGame, procUnit);
        return null;
    }

}
