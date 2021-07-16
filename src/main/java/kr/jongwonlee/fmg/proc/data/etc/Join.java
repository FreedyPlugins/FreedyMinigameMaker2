package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = "join")
public class Join implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String value = process.run(miniGame, procUnit);
        try {
            GameStore.getGame(value).join(procUnit.target.player.getUniqueId());
        } catch (Exception ignored) { }
        return value;
    }

    @Override
    public ProcType getType() {
        return ProcType.JOIN;
    }
}
