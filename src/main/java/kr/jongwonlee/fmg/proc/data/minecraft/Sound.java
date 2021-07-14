package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@Processable(alias = {"sound", "playsound"})
public class Sound implements Process {

    private SmallFrontBrace frontBrace;
    boolean isGame;
    boolean isOnline;

    @Override
    public ProcType getType() {
        return ProcType.SOUND;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
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
            if (isOnline) {
                String name = processList.get(0).run(miniGame, procUnit);
                float volume = (float) Double.parseDouble(processList.get(2).run(miniGame, procUnit));
                if (volume < 0) volume = Float.MAX_VALUE;
                float pitch = (float) Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                float finalVolume = volume;
                Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), name, finalVolume, pitch));
            } else if (isGame) {
                String name = processList.get(0).run(miniGame, procUnit);
                float volume = (float) Double.parseDouble(processList.get(2).run(miniGame, procUnit));
                if (volume < 0) volume = Float.MAX_VALUE;
                float pitch = (float) Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                float finalVolume = volume;
                miniGame.getPlayersData().keySet().forEach(uuid -> Bukkit.getPlayer(uuid).playSound(player.getLocation(), name, finalVolume, pitch));
            } else if (player != null) {
                String name = processList.get(0).run(miniGame, procUnit);
                float volume = (float) Double.parseDouble(processList.get(2).run(miniGame, procUnit));
                if (volume < 0) volume = Float.MAX_VALUE;
                float pitch = (float) Double.parseDouble(processList.get(4).run(miniGame, procUnit));
                player.playSound(player.getLocation(), name, volume, pitch);
            }
        } catch (NumberFormatException e) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}