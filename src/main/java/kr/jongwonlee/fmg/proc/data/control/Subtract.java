package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = {"-"})
public class Subtract implements MathOperator {

    Process valueA;
    Process valueB;


    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        FrontBrace frontBrace = parseUnit.getFrontBrace();
        if (frontBrace instanceof SmallFrontBrace) {
            frontBrace.addProc(parseUnit, FileParser.parseProcess(parseUnit, arguments));
            frontBrace.addProc(parseUnit, this);
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (valueA == null) {
            if (valueB == null) {
                return "";
            } else return '-' + valueB.run(miniGame, procUnit);
        } else if (valueB == null) return valueA.run(miniGame, procUnit);
        return add(valueA.run(miniGame, procUnit), valueB.run(miniGame, procUnit));
    }

    @Override
    public ProcType getType() {
        return ProcType.SUBTRACT;
    }

    public String add(String string, String string2) {
        try {
            return parseIfInt(Double.parseDouble(string) - Double.parseDouble(string2));
        } catch (NumberFormatException e) {
            return string + string2;
        }
    }

    @Override
    public void setValueA(Process process) {
        valueA = process;
    }

    @Override
    public void setValueB(Process process) {
        valueB = process;
    }
}
