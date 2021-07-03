package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

import java.util.ArrayList;

@Processable(alias = {"{"})
public class MidFrontBrace implements FrontBrace {

    ProcBundle procBundle;
    Process nextProcess;

    public void addProc(ParseUnit parseUnit, Process process) {
        procBundle.add(process);
    }

    public void addSkipProc(Process process) {
        this.nextProcess = process;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        if (procBundle == null) {
            parseUnit.addBraceProc(this);
            procBundle = new ProcBundle(new ArrayList<>());
        }
        addProc(parseUnit, FileParser.parseProcess(parseUnit, arguments));
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        procBundle.run(miniGame, procUnit);
        nextProcess.run(miniGame, procUnit);
        String returned = procUnit.getReturned();
        return returned == null ? "" : returned;
    }

    @Override
    public ProcType getType() {
        return ProcType.MID_FRONT_BRACE;
    }

}
