package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.FileParser;
import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.Processable;
import kr.jongwonlee.fmg.proc.ParseUnit;

@Processable(alias = {"}"})
public class MidEndBrace implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        FrontBrace frontBrace = parseUnit.getFrontBrace();
        if (frontBrace instanceof MidFrontBrace) {
            parseUnit.removeBraceProc();
            MidFrontBrace midFrontBrace = (MidFrontBrace) frontBrace;
            process = FileParser.parseProcess(parseUnit, arguments);
            midFrontBrace.addSkipProc(process);
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.MID_END_BRACE;
    }

}
