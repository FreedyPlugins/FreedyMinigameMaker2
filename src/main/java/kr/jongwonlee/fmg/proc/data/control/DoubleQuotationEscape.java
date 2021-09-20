package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = "\\\"")
public class DoubleQuotationEscape implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return "\"" + process.run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.DOUBLE_QUOTATION_ESCAPE;
    }
}
