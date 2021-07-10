package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

import java.util.Iterator;
import java.util.List;

@Processable(alias = "if")
public class If extends ConditionOperator {

    private SmallFrontBrace frontBrace;
    private boolean result = true;

    public boolean isResult() {
        return result;
    }

    @Override
    public ProcType getType() {
        return ProcType.IF;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        parseUnit.addIf(this);
        Process parseProcess = FileParser.parseProcess(parseUnit, arguments);
        if (!(parseProcess instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) parseProcess);
        List<Process> processList = frontBrace.getProcessList();
        if (processList.size() == 0) return;
        Process process = processList.get(processList.size() - 1);
        if (process.getType() == ProcType.SMALL_END_BRACE) {
            processList.add(FileParser.getOneMoreLine(parseUnit, ""));
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) return "";
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
                    processList.get(processList.size() - 1).run(miniGame, procUnit);
                    return "";
                }
                else if (!result && compareType == ProcType.AND) return "";
            } else if (process instanceof SmallEndBrace && result) {
                iterator.next().run(miniGame, procUnit);
                return "";
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

}
