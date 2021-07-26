package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;

//@Processable(alias = {"fly", "flying"})
public class Flying implements Process {

    Process process;
    SmallFrontBrace frontBrace;
    boolean isSet;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String value = process.run(miniGame, procUnit);
        try {
        } catch (Exception ignored) {
            return value;
        }
        return value;
    }

    @Override
    public ProcType getType() {
        return ProcType.FLYING;
    }
}
