package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;

@Processable(alias = {"int"})
public class Int implements Process {

    Process process;

    @Override
    public ProcType getType() {
        return ProcType.INT;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    public String toInt(String string) {
        try {
            return String.valueOf(Double.valueOf(string).intValue());
        } catch (NumberFormatException e) {
            return string;
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return toInt(process.run(miniGame, procUnit));
    }

}
