package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Processable(alias = {"execute"})
public class Execute implements Process {

    Process process;
    boolean isPlayer;

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isPlayer = parseUnit.useExecutor(ProcType.EXECUTE_PLAYER);
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String value = process.run(miniGame, procUnit);
        Player player = procUnit.target.player;
        if (isPlayer && player != null) player.performCommand(value);
        else Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
        return value;
    }

}
