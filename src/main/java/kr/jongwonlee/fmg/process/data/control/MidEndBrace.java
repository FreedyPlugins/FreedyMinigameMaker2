package kr.jongwonlee.fmg.process.data.control;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.util.ParseUnit;

@Processable(alias = {"}"})
public class MidEndBrace extends Process {
    @Override
    public void parse(ParseUnit parseUnit, String arguments) {

    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return null;
    }

    @Override
    public ProcType getType() {
        return null;
    }
}
