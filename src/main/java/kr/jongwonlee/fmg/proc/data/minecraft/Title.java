package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Bukkit;

import java.util.List;

@Processable(alias = {"sendtitle", "title"})
public class Title implements Process {

    private SmallFrontBrace frontBrace;
    boolean isGame;
    boolean isOnline;

    @Override
    public ProcType getType() {
        return ProcType.TITLE;
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
            String fadeIn = processList.get(0).run(miniGame, procUnit);
            String keeping = processList.get(2).run(miniGame, procUnit);
            String fadeOut = processList.get(4).run(miniGame, procUnit);
            String title = processList.get(6).run(miniGame, procUnit);
            String subTitle = processList.get(8).run(miniGame, procUnit);
            if (isGame) miniGame.getPlayers().forEach(uuid -> Bukkit.getPlayer(uuid).sendTitle(title, subTitle, Integer.parseInt(fadeIn), Integer.parseInt(keeping), Integer.parseInt(fadeOut)));
            else if (isOnline) Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(title, subTitle, Integer.parseInt(fadeIn), Integer.parseInt(keeping), Integer.parseInt(fadeOut)));
            else if (procUnit.target.player != null) procUnit.target.player.sendTitle(title, subTitle, Integer.parseInt(fadeIn), Integer.parseInt(keeping), Integer.parseInt(fadeOut));
        } catch (NumberFormatException e) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}