package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

import java.util.function.BiConsumer;

@Processable(alias = {"%"})
public class Remainder implements MathOperator {

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
        if (valueA == null | valueB == null) return "";
        return multiply(valueA.run(miniGame, procUnit), valueB.run(miniGame, procUnit));
    }

    @Override
    public ProcType getType() {
        return ProcType.REMAINDER;
    }

    public String multiply(String string, String string2) {
        return calculate(string, string2);
    }

    @Override
    public double getCalculator(double numA, double numB) {
        return numA % numB;
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
