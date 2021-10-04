package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import org.bukkit.Bukkit;

@Processable(alias = {"sendmessage", "sendmsg", "message", "msg", "send", "print", "say"})
public class SendMessage implements Process {

    private Process process;
    boolean isGame;
    boolean isOnline;

    @Override
    public ProcType getType() {
        return ProcType.SEND_MESSAGE;
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
        if (isGame) miniGame.getPlayers().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(message));
        else if (isOnline) Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
        else if (procUnit.target.player != null) procUnit.target.player.sendMessage(message);
        return message;
    }

}