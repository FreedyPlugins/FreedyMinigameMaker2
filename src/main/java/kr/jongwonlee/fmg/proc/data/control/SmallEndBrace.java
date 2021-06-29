package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.Processable;
import kr.jongwonlee.fmg.proc.FileParser;
import kr.jongwonlee.fmg.proc.ParseUnit;

@Processable(alias = {")"})
public class SmallEndBrace implements Process {

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        FrontBrace frontBrace = parseUnit.getFrontBrace();
        if (frontBrace instanceof SmallFrontBrace) {
            parseUnit.removeBraceProc();
            frontBrace.addProc(FileParser.parseProcess(parseUnit, arguments));
            frontBrace.addProc(this);
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.SMALL_END_BRACE;
    }

}
