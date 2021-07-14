package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;

@Processable(alias = {"gamemode"})
public class GameMode implements Process {

    Process process;


    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            String value = process.run(miniGame, procUnit);
            try {
                procUnit.target.player.setGameMode(org.bukkit.GameMode.getByValue(Integer.parseInt(value)));
            } catch (Exception ignored) {
                procUnit.target.player.setGameMode(org.bukkit.GameMode.valueOf(value.toUpperCase()));
            }
            return "";
        } catch (Exception ignored) {
            return "";
        }
    }

    @Override
    public ProcType getType() {
        return ProcType.GAME_MODE;
    }
}
