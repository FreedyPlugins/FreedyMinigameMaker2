package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@Processable(alias = "NOTHING")  //must be capital letter to prevent notice
public class Nothing implements Process {

    String value;
    Process process;

    public String getValue() {
        return value;
    }

    @Override
    public ProcType getType() {
        return ProcType.NOTHING;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        String args = FileParser.cutFrontSpace(arguments);
        if (args.length() == 0) {
            return;
        }
        FileParser.IndexResult indexResult = FileParser.getStartIndexResult(args);
        if (indexResult.startIndex == -1) {
            value = args;
        } else {
            process = FileParser.parseProcess(parseUnit, args.substring(indexResult.endIndex));
            value = args.substring(0, indexResult.startIndex);
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (process == null) {
            if (value == null) return "";
            else return value;
        } else if (value == null)
            return process.run(miniGame, procUnit);
        return value + process.run(miniGame, procUnit);
    }

}