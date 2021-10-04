package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

@Processable(alias = {"sendactionbar", "actionbar"})
public class ActionBar implements Process {

    private Process process;
    boolean isGame;
    boolean isOnline;

    @Override
    public ProcType getType() {
        return ProcType.ACTION_BAR;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        String message = process.run(miniGame, procUnit);
        if (isGame) miniGame.getPlayers().forEach(uuid -> Bukkit.getPlayer(uuid).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message)));
        else if (isOnline) Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message)));
        else if (procUnit.target.player != null) procUnit.target.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        return message;
    }

}