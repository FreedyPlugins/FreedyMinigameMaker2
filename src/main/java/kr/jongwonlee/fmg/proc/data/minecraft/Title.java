package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;

import java.util.List;

@Processable(alias = {"sendtitle", "title"})
public class Title implements Process {

    private SmallFrontBrace frontBrace;
    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.TITLE;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            List<Process> processList = frontBrace.getProcessList();
            if (procUnit.target.player != null) {
                String fadeIn = processList.get(0).run(miniGame, procUnit);
                String keeping = processList.get(2).run(miniGame, procUnit);
                String fadeOut = processList.get(4).run(miniGame, procUnit);
                String title = processList.get(6).run(miniGame, procUnit);
                String subTitle = processList.get(8).run(miniGame, procUnit);
                procUnit.target.player.sendTitle(title, subTitle, Integer.parseInt(fadeIn), Integer.parseInt(keeping), Integer.parseInt(fadeOut));
            }
        } catch (NumberFormatException e) {
            return "";
        }
        return "";
    }

}