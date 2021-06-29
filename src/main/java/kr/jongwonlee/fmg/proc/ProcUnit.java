package kr.jongwonlee.fmg.proc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ProcUnit {

    public final ProcTarget target;
    private int state;
    private boolean set;
    private boolean game;
    private boolean online;
    private boolean player;
//    private final List<ProcType> processList;
    private final List<ProcType> smallBraceList;

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

    public ProcType getSmallBraceProc() {
        if (smallBraceList.size() == 0) return null;
        return smallBraceList.get(smallBraceList.size() - 1);
    }

    public void addSmallBraceProc(ProcType procType) {
        smallBraceList.add(procType);
    }

    /*
    public void addProcType(ProcType procType) {
        processList.add(procType);
    }

    public ProcType getLatestProcType() {
        if (processList.size() == 0) return null;
        else return processList.get(processList.size() - 1);
    }*/

    public ProcUnit(ProcTarget target) {
        this.target = target;
        smallBraceList = new ArrayList<>();
//        processList = new ArrayList<>();
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
