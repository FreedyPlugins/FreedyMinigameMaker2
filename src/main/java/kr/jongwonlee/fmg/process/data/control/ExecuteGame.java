package kr.jongwonlee.fmg.process.data.control;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;

@Processable(alias = {"game", "minigame"})
public class ExecuteGame extends Process {

    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.EXECUTE_GAME;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        procUnit.setGame(true);
        return process.run(miniGame, procUnit);
    }

}
