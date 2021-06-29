package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.Processable;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.parse.FileParser;
import kr.jongwonlee.fmg.parse.ParseUnit;

import java.util.ArrayList;
import java.util.List;

@Processable(alias = "if")
public class If implements Process {

    List<Condition> conditionList;
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
        Process parseProcess = FileParser.parseProcess(parseUnit, arguments);
        if (!(parseProcess instanceof SmallFrontBrace)) return;
        conditionList = new ArrayList<>();
        SmallFrontBrace frontBrace = ((SmallFrontBrace) parseProcess);
        List<Process> processList = new ArrayList<>(frontBrace.getProcessList());
        List<List<Process>> splitList = new ArrayList<>();
        while (processList.size() == 0) {
            Process process = processList.get(0);
            if (process.getType() == ProcType.OR) splitList.subList(0, );
        }
        while (processList.size() == 0){
            Process process = processList.get(0);
            switch (process.getType()) {
                case IF_BIG:
                case IF_EQUAL:
                case IF_BIG_SAME:
                case IF_SMALL:
                case IF_NOT_EQUAL:
                case IF_SMALL_SAME: {
                    break;
                }
            }
            Condition condition = new Condition();
            conditionList.add(condition);
        }
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
class Condition {

    Process valueA;
    ProcType operator;
    Process valueB;
    Process body;



}