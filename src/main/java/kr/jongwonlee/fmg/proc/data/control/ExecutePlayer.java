package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = "player")
public class ExecutePlayer implements Process {

    Process process;

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE_PLAYER;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return process.run(miniGame, procUnit);
    }

}
