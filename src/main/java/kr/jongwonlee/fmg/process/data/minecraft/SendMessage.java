package kr.jongwonlee.fmg.process.data.minecraft;

import kr.jongwonlee.fmg.MiniGame;
import kr.jongwonlee.fmg.process.Processable;
import kr.jongwonlee.fmg.process.ProcType;
import kr.jongwonlee.fmg.process.ProcUnit;
import kr.jongwonlee.fmg.process.Process;
import kr.jongwonlee.fmg.util.FileParser;
import kr.jongwonlee.fmg.util.ParseUnit;

@Processable(alias = {"sendmessage", "sendmsg", "message", "msg", "send", "print", "say"})
public class SendMessage extends Process {

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