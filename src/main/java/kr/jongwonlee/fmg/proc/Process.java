package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.parse.ParseUnit;

public interface Process {

    void parse(ParseUnit parseUnit, String arguments);

    String run(MiniGame miniGame, ProcUnit procUnit);

    ProcType getType();



}
