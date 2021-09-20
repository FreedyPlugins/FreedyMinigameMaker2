package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;

@Processable(alias = "\"")
public class DoubleQuotation implements Process {

    String string;
    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        int index = arguments.indexOf("\"");
        if (index == -1) {
            string = arguments;
        } else{
            string = arguments.substring(0, index);
            if (arguments.length() <= ++index) {
                process = FileParser.parseProcess(parseUnit, arguments.substring(index));
            }
        }

    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (process == null) return string;
        return string + process.run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.DOUBLE_QUOTATION;
    }
}
