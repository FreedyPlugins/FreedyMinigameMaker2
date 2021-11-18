package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = "taskid")
public class TaskId implements Process {

    boolean isSet;
    Process process;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (isSet) {
            try {
                int taskId = Integer.parseInt(process.run(miniGame, procUnit));
                procUnit.setTaskId(taskId);
                return String.valueOf(taskId);
            } catch(Exception ignored) {
                return "";
            }
        }
        return procUnit.getTaskId() + process.run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.TASK_ID;
    }
}
