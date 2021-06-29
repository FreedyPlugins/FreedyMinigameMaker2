package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.parse.FileParser;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.parse.ParseUnit;
import kr.jongwonlee.fmg.proc.Process;

import java.util.ArrayList;

@Processable(alias = {"{"})
public class MidFrontBrace implements FrontBrace {

    ProcBundle procBundle;
    Process skipProcess;

    public void addProc(Process process) {
        procBundle.add(process);
    }

    public void addSkipProc(Process process) {
        this.skipProcess = process;
    }

    public String skip(MiniGame miniGame, ProcUnit procUnit) {
        return skipProcess.run(miniGame, procUnit);
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        if (procBundle == null) {
            parseUnit.addBraceProc(this);
            procBundle = new ProcBundle(new ArrayList<>());
        }
        addProc(FileParser.parseProcess(parseUnit, arguments));
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        procBundle.run(miniGame, procUnit);
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.MID_FRONT_BRACE;
    }

}
