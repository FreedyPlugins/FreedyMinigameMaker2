package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.Processable;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.FileParser;
import kr.jongwonlee.fmg.proc.ParseUnit;

import java.util.Iterator;
import java.util.List;

@Processable(alias = "if")
public class If implements Process {

    private SmallFrontBrace frontBrace;

    @Override
    public ProcType getType() {
        return ProcType.IF;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        Process parseProcess = FileParser.parseProcess(parseUnit, arguments);
        if (!(parseProcess instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) parseProcess);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        boolean result = false;
        Process tempProc = null;
        ProcType compareType;
        ProcType conditionType = null;
        List<Process> processList = frontBrace.getProcessList();
        Iterator<Process> iterator = processList.iterator();
        while (iterator.hasNext()) {
            Process process = iterator.next();
            if (process instanceof IfOperator) {
                conditionType = process.getType();
            } else if (process instanceof CompareOperator) {
                compareType = process.getType();
                if (result && compareType == ProcType.OR) {
                    return processList.get(processList.size() - 1).run(miniGame, procUnit);
                }
                else if (!result && compareType == ProcType.AND) return "";
            } else if (process instanceof SmallEndBrace && result) {
                return iterator.next().run(miniGame, procUnit);
            } else {
                if (tempProc != null && conditionType != null) {
                    String valueA = tempProc.run(miniGame, procUnit);
                    String valueB = process.run(miniGame, procUnit);
                    result = getValue(valueA, valueB, conditionType);
                    conditionType = null;
                }
                tempProc = process;
            }
        }
        return "";
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
