package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;

import java.util.List;

@Processable(alias = {"data", "var"})
public class Data implements Process {

    SmallFrontBrace frontBrace;
//    boolean isAdd;
    boolean isGame;
    boolean isSet;
    boolean isOnline;

    @Override
    public ProcType getType() {
        return ProcType.DATA;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
//        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) return "";
        List<Process> processList = frontBrace.getProcessList();
            String name = processList.get(0).run(miniGame, procUnit);
            if (isSet) {
                String value = processList.get(2).run(miniGame, procUnit);
                if (isGame) miniGame.getGameData().setData(name, value);
                else if (isOnline) GameDataStore.getInst().setData(name, value);
                else if (procUnit.target.player != null) miniGame.getPlayerData(procUnit.target.player.getUniqueId()).setData(name, value);
                return value + frontBrace.getLastProc().run(miniGame, procUnit);
            } else {
                if (isGame) return miniGame.getGameData().getData(name) + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isOnline) return GameDataStore.getInst().getData(name) + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (procUnit.target.player != null) return miniGame.getPlayerData(procUnit.target.player.getUniqueId()).getData(name) + frontBrace.getLastProc().run(miniGame, procUnit);
            }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}
