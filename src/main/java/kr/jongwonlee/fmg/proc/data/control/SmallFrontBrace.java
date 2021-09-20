package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Processable(alias = {"("})
public class SmallFrontBrace implements FrontBrace {

    List<Process> processList;

    public List<Process> getProcessList() {
        return processList;
    }

    public Process getLastProc() {
        return processList.get(processList.size() - 1);
    }

    public void addProc(ParseUnit parseUnit, Process process) {
        processList.add(process);
    }


    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        processList = new ArrayList<>();
        parseUnit.addBraceProc(this);
        addProc(parseUnit, FileParser.parseProcess(parseUnit, arguments));
        Collections.reverse(processList);
        Bukkit.broadcastMessage(processList.stream().map(process -> process.getType().name()).collect(Collectors.toList()).toString());
        for (int i = 1; i + 1 < processList.size(); i++) {
            Process process = processList.get(i);
            if (process instanceof MathOperator) {
                MathOperator mathOperator = ((MathOperator) process);
                Process valueA = processList.get(i - 1);
                Process valueB = processList.get(i + 1);
                if (valueB instanceof FrontBrace) continue;
                processList.remove(i - 1);
                processList.remove(i);
                mathOperator.setValueA(valueA);
                mathOperator.setValueB(valueB);
            }
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (processList.isEmpty()) return "";
        else return processList.get(0).run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.SMALL_FRONT_BRACE;
    }

    public List<Process> cutBehindEndBrace() {
        List<Process> procTypeList = new ArrayList<>(processList);
        for (int i = 0; i < procTypeList.size(); i++) {
            Process proc = procTypeList.get(i);
            if (proc.getType() == ProcType.SMALL_END_BRACE) {
                return procTypeList.subList(0, i);
            }
        }
        return procTypeList;
    }

}
