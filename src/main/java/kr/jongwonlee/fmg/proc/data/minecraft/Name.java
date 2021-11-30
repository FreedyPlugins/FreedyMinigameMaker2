package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Processable(alias = {"name"})
public class Name implements Process {

    Process process;
    boolean isPlayer;
    boolean isEntity;
    boolean isGame;
    boolean isSet;
    boolean isDisplay;
    boolean isList;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isEntity = parseUnit.useExecutor(ProcType.EXECUTE_ENTITY);
        isPlayer = parseUnit.useExecutor(ProcType.EXECUTE_PLAYER);
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isDisplay = parseUnit.useExecutor(ProcType.EXECUTE_DISPLAY);
        isList = parseUnit.useExecutor(ProcType.LIST);
        if (!isPlayer && !isGame) parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        Player player = procUnit.target.player;
        Entity entity = procUnit.target.entity;
        if (isSet) {
            if (isPlayer) player.setDisplayName(message);
            else player.setPlayerListName(message);
        } else if (isGame) return GameStore.getGame(player).getName();
        else if (isPlayer) {
            if (player != null) {
                if (isList) return player.getPlayerListName() + message;
                else if (isDisplay) return player.getDisplayName() + message;
                else return player.getName() + message;
            }
        } else if (isEntity) {
            if (entity != null) return entity.getName() + message;
        }
        return message;
    }

    @Override
    public ProcType getType() {
        return ProcType.NAME;
    }
}
