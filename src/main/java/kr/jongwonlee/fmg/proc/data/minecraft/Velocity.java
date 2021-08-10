package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;

@Processable(alias = "velocity")
public class Velocity implements Process {

    Process process;
    List<Process> processList;
    SmallFrontBrace frontBrace;
    boolean isSet;
    boolean isAdd;
    boolean isEntity;
    boolean isGet;
    boolean posX;
    boolean posY;
    boolean posZ;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isGet = parseUnit.useExecutor(ProcType.EXECUTE_GET);
        isEntity = parseUnit.useExecutor(ProcType.EXECUTE_ENTITY);
        posX = parseUnit.useExecutor(ProcType.EXECUTE_POS_X);
        posY = parseUnit.useExecutor(ProcType.EXECUTE_POS_Y);
        posZ = parseUnit.useExecutor(ProcType.EXECUTE_POS_Z);
        process = FileParser.parseProcess(parseUnit, arguments);
        if (process instanceof SmallFrontBrace) {
            frontBrace = ((SmallFrontBrace) process);
            processList = frontBrace.cutBehindEndBrace();
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            Entity player;
            if (isEntity) player = procUnit.target.entity;
            else player = procUnit.target.player;
            if (isGet) {
                Vector velocity = player.getVelocity();
                if (posX) return velocity.getX() + process.run(miniGame, procUnit);
                else if (posY) return velocity.getY() + process.run(miniGame, procUnit);
                else if (posZ) return velocity.getZ() + process.run(miniGame, procUnit);
            }
            if (frontBrace == null) return process.run(miniGame, procUnit);
            Process proc1 = processList.get(0);
            String value1 = proc1.run(miniGame, procUnit);
            Process proc2 = processList.get(2);
            String value2 = proc2.run(miniGame, procUnit);
            Process proc3 = processList.get(4);
            String value3 = proc3.run(miniGame, procUnit);
            Vector vector = new Vector(Double.parseDouble(value1), Double.parseDouble(value2), Double.parseDouble(value3));
            if (isSet) player.setVelocity(vector);
            else if (isAdd) player.setVelocity(player.getVelocity().add(vector));
        } catch (Exception ignored) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.VELOCITY;
    }
}
