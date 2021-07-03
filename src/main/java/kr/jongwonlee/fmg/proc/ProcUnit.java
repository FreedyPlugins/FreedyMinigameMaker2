package kr.jongwonlee.fmg.proc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ProcUnit {

    public final ProcTarget target;
    private boolean add;
    private boolean set;
    private boolean game;
    private boolean online;
    private boolean player;
    private String returned;

    public boolean isAdd() {
        return add;
    }

    public boolean isSet() {
        return set;
    }

    public boolean isGame() {
        return game;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

    public void setGame(boolean game) {
        this.game = game;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

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
        setSet(false);
        setGame(false);
        setOnline(false);
        setPlayer(false);
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
