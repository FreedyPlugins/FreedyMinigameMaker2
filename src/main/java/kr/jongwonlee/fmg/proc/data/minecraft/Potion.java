package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

@Processable(alias = {"potion"})
public class Potion implements Process {

    private SmallFrontBrace frontBrace;
    boolean isAdd;
    boolean isRemove;
    boolean isClear;

    @Override
    public ProcType getType() {
        return ProcType.POTION;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        isClear = parseUnit.useExecutor(ProcType.EXECUTE_CLEAR);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            Player player = procUnit.target.player;
            if (player != null) {
                if (isAdd) {
                    List<Process> processList = frontBrace.getProcessList();
                    String potion = processList.get(0).run(miniGame, procUnit);
                    int duration = Integer.parseInt(processList.get(2).run(miniGame, procUnit));
                    int amplifier = Integer.parseInt(processList.get(4).run(miniGame, procUnit));
                    player.addPotionEffect(PotionEffectType.getByName(potion).createEffect(duration, amplifier), false);
                } else if (isRemove) {
                    List<Process> processList = frontBrace.getProcessList();
                    String potion = processList.get(0).run(miniGame, procUnit);
                    player.removePotionEffect(PotionEffectType.getByName(potion));
                } else if (isClear) {
                    player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                }

            }
        } catch (Exception e) {
            //add to no such sound GameAlert
            return frontBrace == null ? "" : frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace == null ? "" : frontBrace.getLastProc().run(miniGame, procUnit);
    }

}