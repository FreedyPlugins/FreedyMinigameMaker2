package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

@Processable(alias = {"hotbar"})
public class ExecuteHotBar implements Process {

    Process process;
    boolean isSet;
    boolean isGet;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isGet = parseUnit.useExecutor(ProcType.EXECUTE_GET);
        if (!isSet && !isGet) parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String value = process.run(miniGame, procUnit);
            try {
                if (isSet) procUnit.target.player.getInventory().setHeldItemSlot(Integer.parseInt(value));
                else if (isGet) return procUnit.target.player.getInventory().getHeldItemSlot() + value;
            } catch (Exception ignored) {
                return value;
            }
        return value;

    }

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE_HOT_BAR;
    }
}
