package kr.jongwonlee.fmg.process.data.etc;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;

@Processable(alias = "timings")
public class Timings extends Process {

    Process process;

    @Override
    public ProcType getType() {
        return ProcType.TIMINGS;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        long before = System.currentTimeMillis();
        process.run(miniGame, procUnit);
        long after = System.currentTimeMillis();
        return String.valueOf(after - before);
    }

}
