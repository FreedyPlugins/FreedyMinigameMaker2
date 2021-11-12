package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.GameData;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;

public interface Process {

    void parse(ParseUnit parseUnit, String arguments);

    String run(MiniGame miniGame, ProcUnit procUnit);

    ProcType getType();

    default GameData getGameData(MiniGame miniGame, ProcUnit procUnit, ProcType procType) {
        switch (procType) {
            case EXECUTE_GAME: return miniGame.getGameData();
            case EXECUTE_ONLINE: return GameDataStore.getInst();
            default: return GameStore.getPlayerData(procUnit.target.player.getUniqueId());
        }
    }

    default ProcType getGameData(ParseUnit parseUnit) {
        if (parseUnit.useExecutor(ProcType.EXECUTE_ONLINE)) return ProcType.EXECUTE_ONLINE;
        else if (parseUnit.useExecutor(ProcType.EXECUTE_GAME)) return ProcType.EXECUTE_GAME;
        else return ProcType.EXECUTE_PLAYER;

    }

}
