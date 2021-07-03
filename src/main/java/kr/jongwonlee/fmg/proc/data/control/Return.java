package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = "return")
public class Return implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        procUnit.setReturned(process.run(miniGame, procUnit));
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.RETURN;
    }
}
