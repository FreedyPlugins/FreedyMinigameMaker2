package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = "player")
public class ExecutePlayer implements Process {

    Process process;

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE_PLAYER;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return process.run(miniGame, procUnit);
    }


/*    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) {
            parseUnit.addExecutor(getType());
            this.process = FileParser.parseProcess(parseUnit, arguments);
        }
        else frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontBrace == null) {
            return process.run(miniGame, procUnit);
        }
        org.bukkit.entity.Player originPlayer = procUnit.target.player;
        try {
            List<Process> processList = frontBrace.getProcessList();
            String name = processList.get(0).run(miniGame, procUnit);
            try {
                procUnit.target.player = Bukkit.getPlayer(FastUUID.parseUUID(name));
            } catch (Exception ignored) {
                org.bukkit.entity.Player player = Bukkit.getPlayer(name);
                if (player != null) procUnit.target.player = player;
            }
            procUnit.target.player = originPlayer;
            return processList.get(processList.size() - 1).run(miniGame, procUnit);
        } catch (Exception e) {
            procUnit.target.player = originPlayer;
            return "";
        }
    }*/

}
