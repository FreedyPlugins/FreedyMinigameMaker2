package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.entity.Player;

@Processable(alias = {"sneak", "sneaking"})
public class Sneaking implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        Player player = procUnit.target.player;
        if (player != null) return player.isSneaking() ? "true" : "false";
        else return "null";
    }

    @Override
    public ProcType getType() {
        return ProcType.SNEAKING;
    }
}
