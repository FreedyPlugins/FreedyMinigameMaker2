package kr.jongwonlee.fmg.process;

import kr.jongwonlee.fmg.MiniGame;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class ProcBundle {

    private final List<Process> processList;

    public ProcBundle(List<Process> processList) {
        this.processList = processList;
    }

    public void run(MiniGame miniGame) {
        run(miniGame, new ProcUnit(new ProcTarget()));
    }

    public void run(MiniGame miniGame, Player player) {
        run(miniGame, ProcUnit.getNewProcUnit(player));
    }

    public void run(MiniGame miniGame, Entity entity) {
        run(miniGame, ProcUnit.getNewProcUnit(entity));
    }

    public void run(MiniGame miniGame, ProcUnit procUnit) {
        processList.forEach(processable -> processable.run(miniGame, procUnit.reset()));
    }

}
