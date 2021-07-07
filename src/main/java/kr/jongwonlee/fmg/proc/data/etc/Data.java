package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = {"data", "var"})
public class Data implements Process {

    Process process;
    boolean isAdd;
    boolean isGame;
    boolean isSet;

    public boolean isAdd() {
        return isAdd;
    }

    public boolean isGame() {
        return isGame;
    }

    public boolean isSet() {
        return isSet;
    }

    @Override
    public ProcType getType() {
        return ProcType.DATA;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isAdd = parseUnit.useAdd();
        isGame = parseUnit.useGame();
        isSet = parseUnit.useSet();
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    public String add(String string, String string2) {
        try {
            return String.valueOf(Double.parseDouble(string) + Double.parseDouble(string2));
        } catch (NumberFormatException e) {
            return string + string2;
        }
    }

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

}
