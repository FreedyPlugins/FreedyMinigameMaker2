package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.Processable;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.FileParser;
import kr.jongwonlee.fmg.proc.ParseUnit;

@Processable(alias = "do")
public class Do implements Process {

    Process process;

    @Override
    public ProcType getType() {
        return ProcType.DO;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit).toLowerCase();
        miniGame.run(message, new ProcUnit(procUnit.target));
        return message;
    }

}
