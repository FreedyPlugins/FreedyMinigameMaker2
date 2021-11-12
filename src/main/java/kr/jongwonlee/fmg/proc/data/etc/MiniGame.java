package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import org.bukkit.Bukkit;

import java.util.List;

@Processable(alias = "minigame")
public class MiniGame implements Process {

    boolean isCreate;
    boolean isRemove;
    boolean isExists;
    Process process;
    SmallFrontBrace frontBrace;
    List<Process> processList;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isExists = parseUnit.useExecutor(ProcType.EXECUTE_EXISTS);
        isCreate = parseUnit.useExecutor(ProcType.EXECUTE_CREATE);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
        processList = frontBrace.cutBehindEndBrace();
    }

    @Override
    public String run(kr.jongwonlee.fmg.game.MiniGame miniGame, ProcUnit procUnit) {
        String name = process.run(miniGame, procUnit);
        if (frontBrace != null) {
            if (processList.size() >= 2) {
                if (isCreate) {
                    String gameName = processList.get(0).run(miniGame, procUnit);
                    String bundleName = processList.get(2).run(miniGame, procUnit);
                    kr.jongwonlee.fmg.game.MiniGame mg = new kr.jongwonlee.fmg.game.MiniGame(gameName, bundleName);
                    GameStore.createGame(gameName, mg);
                }
            }
            if (run(name)) return String.valueOf(GameStore.isGame(name));
            return frontBrace.getLastProc().run(miniGame, procUnit);
        } else {
            if (run(name)) return String.valueOf(GameStore.isGame(name));
            return name;
        }
    }

    private boolean run(String name) {
        if (isExists) {
            return GameStore.isGame(name);
        }
        else if (isCreate) {
            GameStore.createGame(name);
        } else if (isRemove) {
            if (name.equals(Settings.getHubGameName())) return false;
            kr.jongwonlee.fmg.game.MiniGame game = GameStore.getGame(name);
            game.disable();
            GameStore.unloadGame(name);
        }
        return false;
    }

    @Override
    public ProcType getType() {
        return ProcType.MINI_GAME;
    }
}
