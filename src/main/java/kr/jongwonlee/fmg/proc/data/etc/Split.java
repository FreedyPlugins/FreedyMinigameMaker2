package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;

import java.util.Arrays;
import java.util.List;

@Processable(alias = "split")
public class Split implements Process {

    SmallFrontBrace frontBrace;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            List<Process> processList = frontBrace.getProcessList();
            Process process = processList.get(0);
            String value = process.run(miniGame, procUnit);
            Process process2 = processList.get(2);
            String value2 = process2.run(miniGame, procUnit);
            List<String> strings = Arrays.asList(process.run(miniGame, procUnit).split(value2));
            Process process3 = processList.get(4);
            String value3 = process3.run(miniGame, procUnit);
            if (process3.getType() == ProcType.EXECUTE_GAME) miniGame.getGameData().setList(value3, strings);
            else if (process3.getType() == ProcType.EXECUTE_ONLINE) GameDataStore.getInst().setList(value3, strings);
            else miniGame.getPlayerData(procUnit.target.player.getUniqueId()).setList(value3, strings);
            return "";
        } catch (Exception ignored) {
            return "";
        }
    }

    @Override
    public ProcType getType() {
        return ProcType.SPLIT;
    }
}
