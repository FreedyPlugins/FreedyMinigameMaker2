package kr.jongwonlee.fmg.proc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ProcTarget {

    public Player player;
    public Entity entity;

    public ProcTarget() {

    }

    public ProcTarget(ProcTarget target) {
        player = target.player;
        entity = target.entity;
    }

    public ProcTarget put(Player player) {
        this.player = player;
        return this;
    }

    public ProcTarget put(Entity entity) {
        this.entity = entity;
        return this;
    }

}
