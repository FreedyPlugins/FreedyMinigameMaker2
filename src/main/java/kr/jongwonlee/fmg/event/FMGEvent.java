package kr.jongwonlee.fmg.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FMGEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }



}
