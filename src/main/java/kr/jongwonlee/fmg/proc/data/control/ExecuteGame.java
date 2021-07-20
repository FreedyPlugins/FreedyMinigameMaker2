package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = {"game"})
public class ExecuteGame implements Process {

    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE_GAME;
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
