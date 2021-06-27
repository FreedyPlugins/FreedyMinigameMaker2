package kr.jongwonlee.fmg.process;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.util.ParseUnit;

public abstract class Process {

    public abstract void parse(ParseUnit parseUnit, String arguments);

    public abstract String run(MiniGame miniGame, ProcUnit procUnit);

    public abstract ProcType getType();



}
