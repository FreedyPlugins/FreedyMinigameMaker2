package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
import org.bukkit.entity.Player;

@Processable(alias = "spawn")
public class Spawn implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        Player player = procUnit.target.player;
        if (player != null) player.spigot().respawn();
        return process.run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.SPAWN;
    }
}
