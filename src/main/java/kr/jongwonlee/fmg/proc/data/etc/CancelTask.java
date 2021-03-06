package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Bukkit;

@Processable(alias = "cancel")
public class CancelTask implements Process {

    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            String value = process.run(miniGame, procUnit);
            int taskId = Integer.parseInt(value);
            Bukkit.getScheduler().cancelTask(taskId);
            miniGame.getGameData().removeTaskId(taskId);
            GameDataStore.getInst().removeTaskId(taskId);
            GameStore.getPlayerData(procUnit.target.player.getUniqueId()).removeTaskId(taskId);
        } catch (Exception ignored) { }
        return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.CANCEL_TASK;
    }
}
