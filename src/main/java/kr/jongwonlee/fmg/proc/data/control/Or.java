package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = "||")
public class Or implements CompareOperator {

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        FrontBrace frontBrace = parseUnit.getFrontBrace();
        if (frontBrace instanceof SmallFrontBrace) {
            frontBrace.addProc(parseUnit, FileParser.parseProcess(parseUnit, arguments));
            frontBrace.addProc(parseUnit, this);
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.OR;
    }

}
