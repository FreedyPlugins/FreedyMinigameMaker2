package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
import org.bukkit.entity.Player;

@Processable(alias = {"gamemode"})
public class GameMode implements Process {

    Process process;
    boolean isSet;


    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String value = process.run(miniGame, procUnit);
        Player player = procUnit.target.player;
        try {
            if (isSet) {
                try {
                    player.setGameMode(org.bukkit.GameMode.getByValue(Integer.parseInt(value)));
                } catch (Exception ignored) {
                    player.setGameMode(org.bukkit.GameMode.valueOf(value.toUpperCase()));
                }
            } else {
                return player.getGameMode().name() + value;
            }
            return value;
        } catch (Exception ignored) {
            return value;
        }
    }

    @Override
    public ProcType getType() {
        return ProcType.GAME_MODE;
    }
}
