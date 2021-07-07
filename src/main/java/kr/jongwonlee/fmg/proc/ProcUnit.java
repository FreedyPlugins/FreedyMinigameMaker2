package kr.jongwonlee.fmg.proc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ProcUnit {

    public final ProcTarget target;
    private String returned;


    public String getReturned() {
        return returned;
    }

    public void setReturned(String returned) {
        this.returned = returned;
    }

    public ProcUnit(ProcTarget target) {
        this.target = target;
    }

    public ProcUnit reset() {
        return this;
    }

    public static ProcUnit getNewProcUnit(Player player) {
        return new ProcUnit(new ProcTarget().put(player));
    }

    public static ProcUnit getNewProcUnit(Entity entity) {
        return new ProcUnit(new ProcTarget().put(entity));
    }

    public static ProcUnit getNewProcUnit() {
        return new ProcUnit(new ProcTarget());
    }

}
