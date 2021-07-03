package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias =  "else")
public class Else implements Process {

    If anIf;
    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        anIf = parseUnit.getIf();
        if (anIf != null) process = FileParser.parseProcess(parseUnit, arguments);
        if (FileParser.isEmptyProcess(process)) process = FileParser.getOneMoreLine(parseUnit, "");
        parseUnit.removeIf();
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (anIf != null && !anIf.isResult()) return process.run(miniGame, procUnit);
        else return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.ELSE;
    }
}
