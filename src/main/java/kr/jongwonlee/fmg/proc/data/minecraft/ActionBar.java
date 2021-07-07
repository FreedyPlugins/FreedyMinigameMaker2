package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

@Processable(alias = {"sendactionbar", "actionbar"})
public class ActionBar implements Process {

    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.ACTION_BAR;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        if (procUnit.target.player != null) {
            procUnit.target.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
        return message;
    }

}