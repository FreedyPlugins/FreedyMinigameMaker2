package kr.jongwonlee.fmg.process.data.control;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;

@Processable(alias = {")"})
public class SmallEndBrace extends Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        if (parseUnit.getSmallBraceProc() == null) {
            process = FileParser.parseProcess(parseUnit, arguments);
        } else {
            parseUnit.putArgs(arguments);
            parseUnit.removeSmallBraceProc();
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (process == null) return "";
        return process.run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.SMALL_END_BRACE;
    }

}
