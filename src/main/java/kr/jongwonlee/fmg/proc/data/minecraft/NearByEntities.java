package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;

import java.util.List;

//@Processable(alias = {"nearby", "nearbyentity", "nearbyentities" })
public class NearByEntities implements Process {

    SmallFrontBrace frontBrace;
    List<Process> processList;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
            processList = frontBrace.cutBehindEndBrace();
        } else parseUnit.useExecutor(getType());

    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) {
            return "";
        }
        final org.bukkit.entity.Entity originEntity = procUnit.target.entity;
        try {

            return frontBrace.getLastProc().run(miniGame, procUnit);
        } catch (Exception ignored) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
    }

    @Override
    public ProcType getType() {
        return ProcType.NEAR_BY_ENTITIES;
    }
}
