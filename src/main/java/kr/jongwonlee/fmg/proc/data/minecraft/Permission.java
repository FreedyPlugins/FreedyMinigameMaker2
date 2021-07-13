package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

@Processable(alias = {"permissions", "permission", "perm", "perms"})
public class Permission implements Process {

    private SmallFrontBrace frontBrace;

    @Override
    public ProcType getType() {
        return ProcType.PERMISSION;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace == null) return "";
            List<Process> processList = frontBrace.getProcessList();
            Player player = procUnit.target.player;
            if (player != null) {
                return player.hasPermission(processList.get(0).run(miniGame, procUnit)) ? "true" : "false";
            }
        }
        catch (Exception e) {
            return "";
        }
        return "";
    }

}