package kr.jongwonlee.fmg.process.data.minecraft;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;
import org.bukkit.Bukkit;

@Processable(alias = {"broadcast", "broadcastmessage", "announce", "say"})
public class Broadcast extends Process {

    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.BROADCAST;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        Bukkit.broadcastMessage(message);
        return message;
    }

}
