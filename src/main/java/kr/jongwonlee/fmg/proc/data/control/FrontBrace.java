package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.proc.ParseUnit;
import kr.jongwonlee.fmg.proc.Process;

public interface FrontBrace extends Process {

    void addProc(ParseUnit parseUnit, Process process);

}
