package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Bukkit;

@Processable(alias = {"broadcast", "broadcastmessage", "announce", "say"})
public class Broadcast implements Process {

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
