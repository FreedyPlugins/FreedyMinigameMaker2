package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import net.jafama.FastMath;

@Processable(alias = "tan")
public class MathTan implements Process {
    Process process;
    SmallFrontBrace frontBrace;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace != null) {
                String value = frontBrace.getProcessList().get(0).run(miniGame, procUnit);
                double sum = Double.parseDouble(value);
                return FastMath.tan(sum) + frontBrace.getLastProc().run(miniGame, procUnit);
            } else {
                String value = process.run(miniGame, procUnit);
                double sum = Double.parseDouble(value);
                return String.valueOf(FastMath.tan(sum));
            }
        } catch (Exception ignored) {
            return "";
        }
    }

    @Override
    public ProcType getType() {
        return ProcType.MATH_TAN;
    }
}
