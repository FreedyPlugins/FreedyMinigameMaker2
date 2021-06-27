package kr.jongwonlee.fmg.process.data.etc;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;

@Processable(alias = {"name"})
public class Name extends Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        if (procUnit.target.player != null) return procUnit.target.player.getName() + message;
        else if (procUnit.target.entity != null) return procUnit.target.entity.getName() + message;
        else return miniGame.getName() + message;
    }

    @Override
    public ProcType getType() {
        return ProcType.NAME;
    }
}
