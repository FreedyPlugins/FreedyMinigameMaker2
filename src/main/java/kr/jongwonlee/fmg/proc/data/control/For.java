package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

import java.util.Iterator;
import java.util.List;

@Processable(alias = "for")
public class For extends ConditionOperator {

    List<Process> processList;
    private SmallFrontBrace frontBrace;
    private Process setter;
    private Process adder;
    private boolean result = true;

    public boolean isResult() {
        return result;
    }

    @Override
    public ProcType getType() {
        return ProcType.FOR;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        Process parseProcess = FileParser.parseProcess(parseUnit, arguments);
        if (!(parseProcess instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) parseProcess);
        processList = frontBrace.getProcessList();
        if (processList.size() == 0) return;
        Process process = processList.get(processList.size() - 1);
        if (process.getType() == ProcType.SMALL_END_BRACE) {
            processList.add(FileParser.getOneMoreLine(parseUnit, ""));
        }
        try {
            List<Process> processList = frontBrace.cutBehindEndBrace();
            setter = processList.get(0);
            adder = processList.get(processList.size() - 1);
            int setterIndex = this.processList.indexOf(setter);
            this.processList.remove(setterIndex);
            this.processList.remove(setterIndex);
            int adderIndex = this.processList.indexOf(adder);
            this.processList.remove(adderIndex);
            this.processList.remove(adderIndex - 1);
        } catch (Exception ignored) { }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        setter.run(miniGame, procUnit);
        finalRun(miniGame, procUnit);
        String returned = procUnit.getReturned();
        if (returned != null) return returned;
        return "";
    }

    public void finalRun(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) return;
        Process tempProc = null;
        ProcType compareType;
        ProcType conditionType = null;
        Iterator<Process> iterator = processList.iterator();
        while (iterator.hasNext()) {
            Process process = iterator.next();
            if (process instanceof IfOperator) {
                conditionType = process.getType();
            } else if (process instanceof CompareOperator) {
                compareType = process.getType();
                if (result && compareType == ProcType.OR) {
                    adder.run(miniGame, procUnit);
                    processList.get(processList.size() - 1).run(miniGame, procUnit);
                    String returned = procUnit.getReturned();
                    if (returned != null) return;
                    finalRun(miniGame, procUnit);
                    return;
                }
                else if (!result && compareType == ProcType.AND) return;
            } else if (process instanceof SmallEndBrace && result) {
                adder.run(miniGame, procUnit);
                iterator.next().run(miniGame, procUnit);
                String returned = procUnit.getReturned();
                if (returned != null) return;
                finalRun(miniGame, procUnit);
                return;
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
        return;

    }

}
