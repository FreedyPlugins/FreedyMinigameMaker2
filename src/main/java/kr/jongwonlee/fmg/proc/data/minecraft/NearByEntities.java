package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.entity.Entity;

import java.util.List;

@Processable(alias = {"nearby", "nearbyentity", "nearbyentities" })
public class NearByEntities implements Process {

    SmallFrontBrace frontBrace;
    List<Process> processList;
    Process process;
    boolean isEntity;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isEntity = parseUnit.useExecutor(ProcType.EXECUTE_ENTITY);
        process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
            processList = frontBrace.cutBehindEndBrace();
        } else parseUnit.addExecutor(getType());

    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) {
            return process.run(miniGame, procUnit);
        }
        try {
            Entity originEntity = procUnit.target.entity;
            org.bukkit.entity.Entity entity;
            if (isEntity) entity = procUnit.target.entity;
            else entity = procUnit.target.player;
            Process proc1 = processList.get(0);
            String value1 = proc1.run(miniGame, procUnit);
            Process proc2 = processList.get(2);
            String value2 = proc2.run(miniGame, procUnit);
            Process proc3 = processList.get(4);
            String value3 = proc3.run(miniGame, procUnit);
            double x = Double.parseDouble(value1);
            double y = Double.parseDouble(value2);
            double z = Double.parseDouble(value3);
            for (Entity e : entity.getNearbyEntities(x, y, z)) {
                procUnit.target.entity = e;
                frontBrace.getLastProc().run(miniGame, procUnit);
            }
            procUnit.target.entity = originEntity;
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
