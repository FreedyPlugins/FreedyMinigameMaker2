package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.entity.Player;

@Processable(alias = {"name"})
public class Name implements Process {

    Process process;
    boolean isPlayer;
    boolean isGame;
    boolean isSet;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isPlayer = parseUnit.useExecutor(ProcType.EXECUTE_PLAYER);
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        if (!isPlayer && !isGame) parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        Player player = procUnit.target.player;
        if (isSet) {
            if (isPlayer) player.setDisplayName(message);
            else player.setPlayerListName(message);
        } else if (isGame) return miniGame.getName() + message;
        else {
            if (player != null) return player.getName() + message;
            else if (procUnit.target.entity != null) return procUnit.target.entity.getName() + message;
        }
        return message;
    }

    @Override
    public ProcType getType() {
        return ProcType.NAME;
    }
}
