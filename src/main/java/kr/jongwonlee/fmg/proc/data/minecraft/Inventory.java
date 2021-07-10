package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

@Processable(alias = {"inventory", "inven", "inv"})
public class Inventory implements Process {

    private Process process;

    @Override
    public ProcType getType() {
        return ProcType.INVENTORY;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        return process.run(miniGame, procUnit);
    }

}