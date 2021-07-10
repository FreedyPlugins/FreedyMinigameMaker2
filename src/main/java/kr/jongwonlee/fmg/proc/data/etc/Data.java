package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.conf.DataStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;

import java.util.List;

@Processable(alias = {"data", "var"})
public class Data implements Process {

    SmallFrontBrace frontBrace;
    boolean isAdd;
    boolean isGame;
    boolean isSet;
    boolean isOnline;

    public boolean isAdd() {
        return isAdd;
    }

    public boolean isGame() {
        return isGame;
    }

    public boolean isSet() {
        return isSet;
    }

    public boolean isOnline() {
        return isOnline;
    }

    @Override
    public ProcType getType() {
        return ProcType.DATA;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
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
            if (isSet()) {
                String value = processList.get(2).run(miniGame, procUnit);
                if (isGame()) miniGame.getGameData().setData(name, value);
                else if (isOnline()) DataStore.setData(name, value);
                else if (procUnit.target.player != null) miniGame.getPlayerData(procUnit.target.player.getUniqueId()).setData(name, value);
                return value;
            } else {
                if (isGame()) return miniGame.getGameData().getData(name);
                else if (isOnline()) return DataStore.getData(name);
                else if (procUnit.target.player != null) return miniGame.getPlayerData(procUnit.target.player.getUniqueId()).getData(name);
            }
        return "";
    }

/*
    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = FileParser.cutFrontSpace(process.run(miniGame, procUnit));
        int index = message.indexOf(' ');
        String name = index <= 0 ? message : message.substring(0, index);
        String value = index <= 0 ? "" : message.substring(index + 1);
        if (isAdd()) {
            if (isGame()) {
                miniGame.getGameData().setData(name, add(miniGame.getGameData().getData(name), value));
            } else {
                GameData gameData = miniGame.getPlayerData(procUnit.target.player.getUniqueId());
                gameData.setData(name, add(gameData.getData(name), value));
            }
        } else if (isSet()) {
            if (isGame()) {
                miniGame.getGameData().setData(name, value);
            } else {
                miniGame.getPlayerData(procUnit.target.player.getUniqueId()).setData(name, value);
            }
        } else {
            if (isGame()) {
                return miniGame.getGameData().getData(name) + ' ' + value;
            } else {
                return getPlayerData(miniGame, procUnit).getData(name) + ' ' + value;
            }
        }
        return value;
    }

        private GameData getPlayerData(MiniGame miniGame, ProcUnit procUnit) {
        return miniGame.getPlayersData().get(procUnit.target.player.getUniqueId());
    }

*/


}
