package kr.jongwonlee.fmg.process.data.etc;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;

@Processable(alias = {"execute"})
public class Execute extends Process {

    Process process;

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return process.run(miniGame, procUnit);
    }

}
