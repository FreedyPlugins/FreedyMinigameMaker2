package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Processable(alias = "if")
public class If extends ConditionOperator {

    private SmallFrontBrace frontBrace;
    private MidFrontBrace midFrontBrace;
    private boolean result = true;
    private int bundleHash;

    public boolean isResult() {
        return result;
    }

    @Override
    public ProcType getType() {
        return ProcType.IF;
    }

    public int getBundleHash() {
        return bundleHash;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        bundleHash = setBundleHash(parseUnit);
        parseUnit.addIf(this);
        Process parseProcess = FileParser.parseProcess(parseUnit, arguments);
        if (!(parseProcess instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) parseProcess);
        List<Process> processList = frontBrace.getProcessList();
        if (processList.size() == 0) return;
        Process process = processList.get(processList.size() - 1);
        if (process instanceof SmallEndBrace) {
            Then then = FileParser.getOneMoreLine(parseUnit, "");
            processList.add(then);
        } else if (process instanceof MidFrontBrace) {
            midFrontBrace = ((MidFrontBrace) process);
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
                    if (midFrontBrace != null) return midFrontBrace.skip(miniGame, procUnit);
                    return "";
                }
                else if (!result && compareType == ProcType.AND) return "";
            } else if (process instanceof SmallEndBrace) {
                if (result) {
                    iterator.next().run(miniGame, procUnit);
                    return "";
                } else {
                    if (midFrontBrace != null) return midFrontBrace.skip(miniGame, procUnit);
                }
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
        if (midFrontBrace != null) return midFrontBrace.skip(miniGame, procUnit);
        return "";
    }

    public static int setBundleHash(ParseUnit parseUnit) {
        List<FrontBrace> braces = parseUnit.getBraces().stream()
                .filter(frontBrace1 -> frontBrace1 instanceof MidFrontBrace).collect(Collectors.toList());
        if (braces.isEmpty()) return parseUnit.hashCode();
        else return braces.get(braces.size() - 1).hashCode();
    }

}
