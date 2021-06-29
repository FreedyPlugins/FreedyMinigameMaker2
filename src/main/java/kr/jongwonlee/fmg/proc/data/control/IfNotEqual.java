package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.Processable;
import kr.jongwonlee.fmg.parse.FileParser;
import kr.jongwonlee.fmg.parse.ParseUnit;

@Processable(alias = {"!=", "/="})
public class IfNotEqual implements Process {

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        FrontBrace frontBrace = parseUnit.getFrontBrace();
        if (frontBrace instanceof SmallFrontBrace) {
            frontBrace.addProc(this);
            frontBrace.addProc(FileParser.parseProcess(parseUnit, arguments));
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.IF_NOT_EQUAL;
    }

}
