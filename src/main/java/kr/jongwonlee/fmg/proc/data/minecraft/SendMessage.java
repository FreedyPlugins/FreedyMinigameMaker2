package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.FrontBrace;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@Processable(alias = {"sendmessage", "sendmsg", "message", "msg", "send", "print", "say"})
public class SendMessage implements Process {

    private Process process;
    private SmallFrontBrace frontBrace;
    private List<Process> processList;
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
        if (process instanceof SmallFrontBrace) {
            frontBrace = (SmallFrontBrace) process;
            processList = frontBrace.cutBehindEndBrace();
        }
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (isGame) miniGame.getPlayers().forEach(uuid -> sendMessage(miniGame, procUnit, Bukkit.getPlayer(uuid)));
        else if (isOnline) Bukkit.getOnlinePlayers().forEach(player -> sendMessage(miniGame, procUnit, player));
        else if (procUnit.target.player != null) return sendMessage(miniGame, procUnit, procUnit.target.player);
        return "";
    }

    public String sendMessage(MiniGame miniGame, ProcUnit procUnit, Player player) {
        String procMessage = process.run(miniGame, procUnit);
        if (frontBrace == null || processList.size()<=4) {
            if (player != null) player.sendMessage(procMessage);
        } else {
            try {
                 ComponentBuilder cb = new ComponentBuilder("");
                for (int i = 0; i < processList.size(); i += 6) {
                    TextComponent message = new TextComponent(processList.get(i).run(miniGame, procUnit));
                    String event = processList.get(i + 2).run(miniGame, procUnit);
                    String value = processList.get(i + 4).run(miniGame, procUnit);
                    HoverEvent.Action hoverAction = getHoverAction(event);
                    if (hoverAction != null) {
                        message.setHoverEvent(new HoverEvent(hoverAction, new BaseComponent[]{new TextComponent(value)}));
                    } else {
                        ClickEvent.Action clickAction = getClickAction(event);
                        if (clickAction != null) {
                            message.setClickEvent(new ClickEvent(clickAction, value));
                        }
                    }
                    cb.append(message);
                }
                player.spigot().sendMessage(cb.create());
            } catch (Exception ignored)  {

            }
        }
        return procMessage;
    }

    public HoverEvent.Action getHoverAction(String string) {
        try {
            return HoverEvent.Action.valueOf(string);
        } catch (Exception e) {
            return null;
        }
    }

    public ClickEvent.Action getClickAction(String string) {
        try {
            return ClickEvent.Action.valueOf(string);
        } catch (Exception e) {
            return null;
        }
    }

}