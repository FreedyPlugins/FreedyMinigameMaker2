package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Bukkit;

@Processable(alias = "then")
public class Then implements FrontBrace {

    Process process;

    public Process getProc() {
        return process;
    }

    @Override
    public void addProc(ParseUnit parseUnit, Process process) {
        this.process = process;
        parseUnit.removeBraceProc(this);
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        parseUnit.addBraceProc(this);
        process = FileParser.parseProcess(parseUnit, arguments);
        if (FileParser.isEmptyProcess(process)) {
            process = null;
            return;
        }
        parseUnit.removeBraceProc(this);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return process.run(miniGame, procUnit);
    }

    @Override
    public ProcType getType() {
        return ProcType.THEN;
    }

}
