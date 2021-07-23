package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@Processable(alias = "particle")
public class Particle implements Process {

    SmallFrontBrace frontBrace;
    List<Process> processList;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) frontBrace = ((SmallFrontBrace) process);
        processList = frontBrace.cutBehindEndBrace();
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            Player player = procUnit.target.player;
            Process proc1 = processList.get(0);
            String value1 = proc1.run(miniGame, procUnit);
            Location location;
            if (proc1.getType() == ProcType.EXECUTE_GAME) location = miniGame.getGameData().getLocation(value1);
            else if (proc1.getType() == ProcType.EXECUTE_ONLINE) location = GameDataStore.getInst().getLocation(value1);
            else location = miniGame.getPlayerData(player.getUniqueId()).getLocation(value1);
            Process proc2 = processList.get(2);
            String value2 = proc2.run(miniGame, procUnit);
            Process proc3 = processList.get(4);
            String value3 = proc3.run(miniGame, procUnit);
            int amount = Integer.parseInt(value3);
            boolean isStatic = processList.size() >= 6;
            if (isStatic) location.getWorld().spawnParticle(org.bukkit.Particle.valueOf(value2), location, amount, 0, 0, 0, 0, 0);
            else location.getWorld().spawnParticle(org.bukkit.Particle.valueOf(value2), location, amount);
        } catch (Exception ignored) {  }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.PARTICLE;
    }
}
