package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = {"set"})
public class ExecuteSet implements Process {

    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE_SET;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        parseUnit.setSet(true);
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        process.run(miniGame, procUnit);
        return null;
    }

}
