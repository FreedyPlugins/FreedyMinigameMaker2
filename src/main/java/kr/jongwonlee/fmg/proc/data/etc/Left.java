package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = "left")
public class Left implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String value = process.run(miniGame, procUnit);
        GameStore.getGames().forEach(game -> {
            if (!game.equals(GameStore.getHubGame())) game.quit(procUnit.target.player.getUniqueId());
        });
        return value;
    }

    @Override
    public ProcType getType() {
        return ProcType.LEFT;
    }
}
