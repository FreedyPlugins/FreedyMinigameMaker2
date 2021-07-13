package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;

import java.util.List;

@Processable(alias = {"refs", "reference"})
public class Refs implements Process {

    SmallFrontBrace frontBrace;

    @Override
    public ProcType getType() {
        return ProcType.REFERENCE;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            List<Process> processList = frontBrace.getProcessList();
            String name = processList.get(0).run(miniGame, procUnit);
            String message = processList.get(2).run(miniGame, procUnit);
            return GameStore.getGame(name).getProcBundle(message).run(miniGame, new ProcUnit(procUnit.target));
        } catch (Exception ignored) {
            return "";
        }

    }

}
