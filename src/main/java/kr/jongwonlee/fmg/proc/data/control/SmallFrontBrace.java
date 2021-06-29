package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.FileParser;
import kr.jongwonlee.fmg.proc.ParseUnit;
import kr.jongwonlee.fmg.proc.Process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Processable(alias = {"("})
public class SmallFrontBrace implements FrontBrace {

    List<Process> processList;

    public List<Process> getProcessList() {
        return processList;
    }

    public void addProc(Process process) {
        processList.add(process);
    }


    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        processList = new ArrayList<>();
        parseUnit.addBraceProc(this);
        addProc(FileParser.parseProcess(parseUnit, arguments));
        Collections.reverse(processList);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.SMALL_FRONT_BRACE;
    }

}
