package kr.jongwonlee.fmg.proc.data.minecraft;

import com.eatthepath.uuid.FastUUID;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

import java.util.Random;
import java.util.UUID;

@Processable(alias = {"uuid"})
public class Uuid implements Process {

    Process process;
    boolean isRandom;
    Random r = new Random();

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isRandom = parseUnit.useExecutor(ProcType.RANDOM);
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        if (isRandom) return FastUUID.toString(new UUID(r.nextLong(), r.nextLong()));
        else if (procUnit.target.player != null) return FastUUID.toString(procUnit.target.player.getUniqueId()) + message;
        else if (procUnit.target.entity != null) return FastUUID.toString(procUnit.target.entity.getUniqueId()) + message;
        else return miniGame.getName() + message;
    }

    @Override
    public ProcType getType() {
        return ProcType.UUID;
    }
}
