package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = {"uuid"})
public class Uuid implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        if (procUnit.target.player != null) return procUnit.target.player.getUniqueId() + message;
        else if (procUnit.target.entity != null) return procUnit.target.entity.getUniqueId() + message;
        else return miniGame.getName() + message;
    }

    @Override
    public ProcType getType() {
        return ProcType.UUID;
    }
}
