package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;

@Processable(alias = "cursor")
public class Cursor implements Process {
    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String value = process.run(miniGame, procUnit);
        try {
            procUnit.target.player.getInventory().setHeldItemSlot(Integer.parseInt(value));
        } catch (Exception ignored) {

        }
        return value;
    }

    @Override
    public ProcType getType() {
        return ProcType.CURSOR;
    }
}
