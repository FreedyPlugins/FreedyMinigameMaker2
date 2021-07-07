package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.proc.data.control.FrontBrace;
import kr.jongwonlee.fmg.proc.data.control.If;

import java.util.ArrayList;
import java.util.List;

public class ParseUnit {

    private final List<FrontBrace> braceList;
    private final List<If> ifList;

    private boolean add;
    private boolean set;
    private boolean game;
    private boolean online;
    private boolean player;
    private boolean async;

    public boolean useAdd() {
        boolean add = this.add;
        setAdd(false);
        return add;
    }

    public boolean useSet() {
        boolean set = this.set;
        setSet(false);
        return set;
    }

    public boolean useGame() {
        boolean game = this.game;
        setGame(false);
        return game;
    }

    public boolean useOnline() {
        boolean online = this.online;
        setOnline(false);
        return online;
    }

    public boolean usePlayer() {
        boolean player = this.player;
        setPlayer(false);
        return player;
    }

    public boolean useAsync() {
        boolean async = this.async;
        setAsync(false);
        return async;
    }

    public boolean isAsync() {
        return async;
    }

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

    public void setAsync(boolean async) {
        this.async = async;
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

    public void removeBraceProc(FrontBrace frontBrace) {
        braceList.remove(frontBrace);
    }

    public void addBraceProc(FrontBrace frontBrace) {
        braceList.add(frontBrace);
    }

    public void removeBraceProc() {
        if (braceList.size() != 0) braceList.remove(braceList.size() - 1);
    }

    public boolean hasBrace() {
        return braceList.size() != 0;
    }

    public FrontBrace getFrontBrace() {
        if (braceList.size() == 0) return null;
        return braceList.get(braceList.size() - 1);
    }

    public void addIf(If anIf) {
        ifList.add(anIf);
    }

    public void removeIf() {
        if (ifList.size() != 0) ifList.remove(ifList.size() - 1);
    }

    public boolean hasIf() {
        return ifList.size() != 0;
    }

    public If getIf() {
        if (ifList.size() == 0) return null;
        return ifList.get(ifList.size() - 1);
    }

    public ParseUnit() {
        this.braceList = new ArrayList<>();
        this.ifList = new ArrayList<>();
    }

    @Deprecated
    public ParseUnit reset() {
        setSet(false);
        setGame(false);
        setOnline(false);
        setPlayer(false);
        return this;
    }

}
