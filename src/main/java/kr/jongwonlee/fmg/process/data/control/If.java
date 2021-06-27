package kr.jongwonlee.fmg.process.data.control;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;
import org.bukkit.Bukkit;

@Processable(alias = "if")
public class If extends Process {

    private Process valueA;
    private ProcType operator;
    private Process valueB;
    private Process body;

    @Override
    public ProcType getType() {
        return ProcType.IF;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        valueA = FileParser.parseProcess(parseUnit, arguments);
        operator = parseUnit.getProcType(0);
        valueB = FileParser.parseProcess(parseUnit, parseUnit.getArgs());
        body = FileParser.parseProcess(parseUnit, parseUnit.getArgs());
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String valueA = this.valueA.run(miniGame, procUnit);
        String valueB = this.valueB.run(miniGame, procUnit);
        if (getValue(valueA, valueB, operator)) {
            body.run(miniGame, procUnit);
            return "true";
        } else return "false";
    }

    private static double toDouble(String string) throws NumberFormatException {
        return Double.parseDouble(string);
    }

    private static boolean getValue(String valueA, String valueB, ProcType operator) {
        try {
            switch (operator) {
                case IF_BIG: return toDouble(valueA) == toDouble(valueB);
                case IF_BIG_SAME: return toDouble(valueA) <= toDouble(valueB);
                case IF_EQUAL: return valueA.equals(valueB);
                case IF_NOT_EQUAL: return !valueA.equals(valueB);
                case IF_SMALL: return toDouble(valueA) > toDouble(valueB);
                case IF_SMALL_SAME: return toDouble(valueA) >= toDouble(valueB);
                default: return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
