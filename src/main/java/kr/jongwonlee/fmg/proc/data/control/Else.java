package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

import java.util.List;

@Processable(alias =  "else")
public class Else implements Process {

    If anIf;
    Process process;
    int bundleHash;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        bundleHash = If.setBundleHash(parseUnit);
        anIf = isIf(parseUnit);
        if (anIf == null) return;
        parseUnit.removeIf(anIf);
        if (anIf != null) {
            process = FileParser.parseProcess(parseUnit, arguments);
        }
        if (FileParser.isEmptyProcess(process)) process = FileParser.getOneMoreLine(parseUnit, "");
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (anIf != null && !anIf.isResult()) return process.run(miniGame, procUnit);
        else return "";
    }

    @Override
    public ProcType getType() {
        return ProcType.ELSE;
    }

    public If isIf(ParseUnit parseUnit) {
        List<If> ifs = parseUnit.getIfs();
        for (If aIf : ifs) {
            if (aIf.getBundleHash() == bundleHash) {
                return aIf;
            }
        }
        return null;
    }

}
