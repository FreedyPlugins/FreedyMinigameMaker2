package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.FMGPlugin;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.entity.Player;

import java.util.List;

@Processable(alias = {"repeat"})
public class Repeat implements Process {

    Process process;
    SmallFrontBrace frontBrace;
    boolean isGame;
    boolean isOnline;

    @Override
    public ProcType getType() {
        return ProcType.REPEAT;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
        List<Process> processList = frontBrace.getProcessList();
        if (processList.size() == 0) return;
        Process process = processList.get(processList.size() - 1);
        if (process.getType() == ProcType.SMALL_END_BRACE) {
            processList.add(FileParser.getOneMoreLine(parseUnit, ""));
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        Player player = procUnit.target.player;
        List<Process> processList = frontBrace.getProcessList();
        String delay = processList.get(0).run(miniGame, procUnit);
        String period = processList.get(2).run(miniGame, procUnit);
        int taskId;
        ProcUnit procUnit2 = new ProcUnit(procUnit.target, procUnit.getTaskId());
        Runnable runnable = () -> frontBrace.getLastProc().run(miniGame, procUnit2);
        taskId = FMGPlugin.runTaskRepeatSync(runnable, ((long) Double.parseDouble(delay)), ((long) Double.parseDouble(period)));
        if (isGame) miniGame.getGameData().addTaskId(taskId);
        else if (!isOnline && player != null) GameStore.getPlayerData(player.getUniqueId()).addTaskId(taskId);
        procUnit2.setTaskId(taskId);
        return String.valueOf(taskId);
    }

}
