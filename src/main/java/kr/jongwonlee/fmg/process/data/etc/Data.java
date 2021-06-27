package kr.jongwonlee.fmg.process.data.etc;

import kr.jongwonlee.fmg.GameData;
import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;
import org.bukkit.Bukkit;

@Processable(alias = {"data", "var"})
public class Data extends Process {

    Process process;

    @Override
    public ProcType getType() {
        return ProcType.DATA;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = FileParser.cutFrontSpace(process.run(miniGame, procUnit));
        int index = message.indexOf(' ');
        String name = index <= 0 ? message : message.substring(0, index);
        String value = index <= 0 ? "" : message.substring(index + 1);
        if (procUnit.isSet()) {
            procUnit.setSet(false);
            if (procUnit.isGame()) {
                miniGame.getGameData().setData(name, value);
                procUnit.setGame(false);
            } else {
                miniGame.getPlayersData().get(procUnit.target.player.getUniqueId()).setData(name, value);
            }
        } else {
            if (procUnit.isGame()) {
                procUnit.setGame(false);
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

}
