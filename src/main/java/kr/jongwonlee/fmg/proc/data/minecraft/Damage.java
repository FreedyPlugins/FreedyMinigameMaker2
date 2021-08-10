package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import net.jafama.FastMath;
import org.bukkit.entity.Player;

@Processable(alias = "damage")
public class Damage implements Process {

    Process process;
    boolean isSet;
    boolean isAdd;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        Player player = procUnit.target.player;
        String value = process.run(miniGame, procUnit);
        try {
            if (isAdd) {
                int food = player.getFoodLevel();
                int amount = Integer.parseInt(value);
                player.setFoodLevel(FastMath.max(0, FastMath.min(20, food + amount)));
            } else if (isSet) {
                int amount = Integer.parseInt(value);
                player.setFoodLevel(FastMath.max(0, FastMath.min(20, amount)));
            } else {
                return player.getFoodLevel() + value;
            }
        } catch (Exception ignored) {
            return value;
        }
        return value;
    }

    @Override
    public ProcType getType() {
        return ProcType.DAMAGE;
    }
}
