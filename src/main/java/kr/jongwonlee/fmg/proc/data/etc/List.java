package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import net.jafama.FastMath;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Processable(alias = {"list"})
public class List implements Process {

    SmallFrontBrace frontBrace;
    boolean isAdd;
    boolean isGame;
    boolean isSet;
    boolean isSize;
    boolean isClear;
    boolean isGet;
    boolean isRemove;

    @Override
    public ProcType getType() {
        return ProcType.LIST;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isSize = parseUnit.useExecutor(ProcType.EXECUTE_SIZE);
        isClear = parseUnit.useExecutor(ProcType.EXECUTE_CLEAR);
        isGet = parseUnit.useExecutor(ProcType.EXECUTE_GET);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        Process process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = ((SmallFrontBrace) process);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace == null) return "";
            java.util.List<Process> processList = frontBrace.getProcessList();
            String name = processList.get(0).run(miniGame, procUnit);
            Player player = procUnit.target.player;
            if (isGame) {
                java.util.List<String> list = miniGame.getGameData().getList(name);
                if (isSize) return String.valueOf(list.size());
                else if (isClear && list != null) {
                    miniGame.getGameData().setList(name, null);
                    return "";
                }
                else {
                    if (isAdd) {
                        if (list == null) {
                            list = new ArrayList<>();
                            miniGame.getGameData().setList(name, list);
                        }
                        String value = processList.get(2).run(miniGame, procUnit);
                        list.add(value);
                    } else if (isRemove) {
                        if (list == null) return "";
                        String value = processList.get(2).run(miniGame, procUnit);
                        list.remove(value);
                    } else if (isGet) {
                        try {
                            String value = processList.get(2).run(miniGame, procUnit);
                            int index = Integer.parseInt(value);
                            if (list == null || list.isEmpty() || index + 1 >= list.size()) return "null";
                            return list.get(FastMath.min(list.size() - 1, index));
                        } catch (Exception e) {
                            return "";
                        }
                    }
                }
            } else if (player != null) {
                java.util.List<String> list = miniGame.getPlayerData(player.getUniqueId()).getList(name);
                    if (isSize) return String.valueOf(list.size());
                    else if (isClear && list != null) {
                        miniGame.getPlayerData(player.getUniqueId()).setList(name, null);
                        return "";
                    } else {
                        if (isAdd) {
                            if (list == null) {
                                list = new ArrayList<>();
                                miniGame.getGameData().setList(name, list);
                            }
                            String value = processList.get(2).run(miniGame, procUnit);
                            list.add(value);
                        } else if (isRemove) {
                            if (list == null) return "";
                            String value = processList.get(2).run(miniGame, procUnit);
                            list.remove(value);
                        } else if (isGet) {
                            try {
                                if (list == null || list.isEmpty()) return "null";
                                String value = processList.get(2).run(miniGame, procUnit);
                                int index = Integer.parseInt(value);
                                return list.get(FastMath.min(list.size() - 1, index));
                            } catch (Exception e) {
                                return "";
                            }
                        }
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

}
