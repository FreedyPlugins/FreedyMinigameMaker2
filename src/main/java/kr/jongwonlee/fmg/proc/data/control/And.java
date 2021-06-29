package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.parse.ParseUnit;
import kr.jongwonlee.fmg.proc.Processable;

@Processable(alias = "&&")
public class And implements Process {


    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        parseUnit.putArgs(arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.AND;
    }
}
