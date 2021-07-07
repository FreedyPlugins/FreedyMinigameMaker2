package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.conf.LocationStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Processable(alias = {"teleport", "tp"})
public class Teleport implements Process {

    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.TELEPORT;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        Player player = procUnit.target.player;
        if (player != null) {
            Location location = LocationStore.getLocation(message);
            if (location != null) player.teleport(location);
        } else {
            Entity entity = procUnit.target.entity;
            Location location = LocationStore.getLocation(message);
            if (location != null) entity.teleport(location);
        }
        return message;
    }

}
