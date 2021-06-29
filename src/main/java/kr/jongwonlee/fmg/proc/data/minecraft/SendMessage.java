package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Processable;
import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.ProcUnit;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.FileParser;
import kr.jongwonlee.fmg.proc.ParseUnit;

@Processable(alias = {"sendmessage", "sendmsg", "message", "msg", "send", "print", "say"})
public class SendMessage implements Process {

    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.SEND_MESSAGE;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        if (procUnit.target.player != null) procUnit.target.player.sendMessage(message);
        return message;
    }

}