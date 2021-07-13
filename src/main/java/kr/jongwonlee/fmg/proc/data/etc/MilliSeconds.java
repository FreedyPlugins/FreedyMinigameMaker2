package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;

@Processable(alias = {"millisec", "millisecond", "milliseconds"})
public class MilliSeconds implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return System.currentTimeMillis() + process.run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.MILLI_SECONDS;
    }
}
