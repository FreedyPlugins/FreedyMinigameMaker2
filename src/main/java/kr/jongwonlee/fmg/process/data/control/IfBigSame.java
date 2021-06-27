package kr.jongwonlee.fmg.process.data.control;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;

@Processable(alias = {">="})
public class IfBigSame extends Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        ProcType procType = parseUnit.getSmallBraceProc();
        if (procType == ProcType.IF) parseUnit.putArgs(arguments);
        else process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (process == null) return "";
        else return process.run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.IF_BIG_SAME;
    }

}
