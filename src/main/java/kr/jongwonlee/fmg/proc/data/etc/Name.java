package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = {"name"})
public class Name implements Process {

    Process process;
    boolean isPlayer;
    boolean isGame;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isPlayer = parseUnit.useExecutor(ProcType.EXECUTE_PLAYER);
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        if (!isPlayer && !isGame) parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        if (isGame) return miniGame.getName() + message;
        else {
            if (procUnit.target.player != null) return procUnit.target.player.getName() + message;
            else if (procUnit.target.entity != null) return procUnit.target.entity.getName() + message;
        }
        return message;
    }

    @Override
    public ProcType getType() {
        return ProcType.NAME;
    }
}
