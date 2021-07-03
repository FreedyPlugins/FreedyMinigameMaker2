package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.game.MiniGame;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class ProcBundle {

    private final List<Process> processList;

    public ProcBundle(List<Process> processList) {
        this.processList = processList;
    }

    public void add(Process process) {
        processList.add(process);
    }

    public String run(MiniGame miniGame) {
        return run(miniGame, new ProcUnit(new ProcTarget()));
    }

    public String run(MiniGame miniGame, Player player) {
        return run(miniGame, ProcUnit.getNewProcUnit(player));
    }

    public void run(MiniGame miniGame, Entity entity) {
        run(miniGame, ProcUnit.getNewProcUnit(entity));
    }

    public String run(MiniGame miniGame, ProcUnit procUnit) {
        for (Process process : processList) {
            if (procUnit.getReturned() == null) process.run(miniGame, procUnit.reset());
            else {
                String returned = procUnit.getReturned();
                procUnit.setReturned(null);
                return returned;
            }
        }
        return "";
    }

}
