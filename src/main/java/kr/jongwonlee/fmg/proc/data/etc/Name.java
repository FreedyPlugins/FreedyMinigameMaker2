package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.Processable;
import kr.jongwonlee.fmg.parse.FileParser;
import kr.jongwonlee.fmg.parse.ParseUnit;

@Processable(alias = {"name"})
public class Name implements Process {

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
