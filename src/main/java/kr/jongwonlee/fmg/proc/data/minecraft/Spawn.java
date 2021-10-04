package kr.jongwonlee.fmg.proc.data.minecraft;

import com.eatthepath.uuid.FastUUID;
import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

@Processable(alias = "spawn")
public class Spawn implements Process {

    Process process;
    SmallFrontBrace frontBrace;
    List<Process> processList;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
            processList = frontBrace.cutBehindEndBrace();
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        Player player = procUnit.target.player;
        if (frontBrace == null) {
            if (player != null) player.spigot().respawn();
            return process.run(miniGame, procUnit);
        }
        try {
            Process proc1 = processList.get(0);
            String value1 = proc1.run(miniGame, procUnit);
            Process proc2 = processList.get(2);
            String value2 = proc2.run(miniGame, procUnit);
            Location location;
            if (proc2.getType() == ProcType.EXECUTE_GAME) location = miniGame.getGameData().getLocation(value2);
            else if (proc2.getType() == ProcType.EXECUTE_ONLINE) location = GameDataStore.getInst().getLocation(value2);
            else if (proc2.getType() == ProcType.EXECUTE_PLAYER) location = player.getLocation();
            else location = GameStore.getPlayerData(player.getUniqueId()).getLocation(value2);
            Entity entity = location.getWorld().spawn(location, EntityType.valueOf(value1).getEntityClass(), e -> {
                Entity origin = procUnit.target.entity;
                procUnit.target.entity = e;
                frontBrace.getLastProc().run(miniGame, procUnit);
                procUnit.target.entity = origin;
            });
            return FastUUID.toString(entity.getUniqueId()) + frontBrace.getLastProc().run(miniGame, procUnit);
        } catch (Exception ignored) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
    }

    @Override
    public ProcType getType() {
        return ProcType.SPAWN;
    }
}
