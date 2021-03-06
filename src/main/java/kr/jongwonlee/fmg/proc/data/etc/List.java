package kr.jongwonlee.fmg.proc.data.etc;

import kr.jongwonlee.fmg.conf.GameDataStore;
import kr.jongwonlee.fmg.game.GameStore;
import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import kr.jongwonlee.fmg.proc.data.control.SmallFrontBrace;
import net.jafama.FastMath;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

@Processable(alias = {"list"})
public class List implements Process {

    SmallFrontBrace frontBrace;
    Process process;
    java.util.List<Process> processList;
    boolean isAdd;
    boolean isGame;
    boolean isOnline;
    boolean isSet;
    boolean isSize;
    boolean isClear;
    boolean isGet;
    boolean isRemove;
    boolean isShuffle;
    boolean isContains;
    boolean isClone;

    @Override
    public ProcType getType() {
        return ProcType.LIST;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isGame = parseUnit.useExecutor(ProcType.EXECUTE_GAME);
        isOnline = parseUnit.useExecutor(ProcType.EXECUTE_ONLINE);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isSize = parseUnit.useExecutor(ProcType.EXECUTE_SIZE);
        isClear = parseUnit.useExecutor(ProcType.EXECUTE_CLEAR);
        isGet = parseUnit.useExecutor(ProcType.EXECUTE_GET);
        isRemove = parseUnit.useExecutor(ProcType.EXECUTE_REMOVE);
        isShuffle = parseUnit.useExecutor(ProcType.EXECUTE_SHUFFLE);
        isContains = parseUnit.useExecutor(ProcType.EXECUTE_CONTAINS);
        isClone = parseUnit.useExecutor(ProcType.EXECUTE_CLONE);
        if (isGet) parseUnit.addExecutor(getType());
        process = FileParser.parseProcess(parseUnit, arguments);
        if (!(process instanceof SmallFrontBrace)) return;
        frontBrace = (SmallFrontBrace) process;
        processList = frontBrace.cutBehindEndBrace();
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        try {
            if (frontBrace == null) return process.run(miniGame, procUnit);
            String name = processList.get(0).run(miniGame, procUnit);
            Player player = procUnit.target.player;
            if (isOnline) {
                java.util.List<String> list = GameDataStore.getInst().getList(name);
                if (isClone) {
                    Process proc2 = processList.get(2);
                    String value2 = proc2.run(miniGame, procUnit);
                    if (proc2.getType() == ProcType.EXECUTE_GAME) miniGame.getGameData().setList(value2, new ArrayList<>(list));
                    else if (proc2.getType() == ProcType.EXECUTE_ONLINE) GameDataStore.getInst().setList(value2, new ArrayList<>(list));
                    else GameStore.getPlayerData(player.getUniqueId()).setList(value2, new ArrayList<>(list));
                } else if (isContains) return list == null ? "false" : list.contains(processList.get(2).run(miniGame, procUnit)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isSize) return list == null ? "0" : list.size() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isShuffle) Collections.shuffle(list);
                else if (isClear && list != null) {
                    GameDataStore.getInst().setList(name, null);
                    return frontBrace.getLastProc().run(miniGame, procUnit) + frontBrace.getLastProc().run(miniGame, procUnit);
                } else {
                    if (isAdd) {
                        if (list == null) {
                            list = new ArrayList<>();
                            GameDataStore.getInst().setList(name, list);
                        }
                        for (int i = 2; i < processList.size(); i += 2) {
                            String value = processList.get(i).run(miniGame, procUnit);
                            list.add(value);
                        }
                    } else if (isRemove) {
                        if (list == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                        String value = processList.get(2).run(miniGame, procUnit);
                        list.remove(value);
                    } else if (isGet) {
                        try {
                            String value = processList.get(2).run(miniGame, procUnit);
                            int index = Integer.parseInt(value);
                            if (list == null || list.isEmpty() || index >= list.size()) return "null" + frontBrace.getLastProc().run(miniGame, procUnit);
                            return list.get(FastMath.min(list.size() - 1, index)) + frontBrace.getLastProc().run(miniGame, procUnit);
                        } catch (Exception e) {
                            return frontBrace.getLastProc().run(miniGame, procUnit);
                        }
                    }
                }
            } else if (isGame) {
                java.util.List<String> list = miniGame.getGameData().getList(name);
                if (isClone) {
                    Process proc2 = processList.get(2);
                    String value2 = proc2.run(miniGame, procUnit);
                    if (proc2.getType() == ProcType.EXECUTE_GAME) miniGame.getGameData().setList(value2, new ArrayList<>(list));
                    else if (proc2.getType() == ProcType.EXECUTE_ONLINE) GameDataStore.getInst().setList(value2, new ArrayList<>(list));
                    else GameStore.getPlayerData(player.getUniqueId()).setList(value2, new ArrayList<>(list));
                } else if (isContains) return list == null ? "false" : list.contains(processList.get(2).run(miniGame, procUnit)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isSize) return list == null ? "0" : list.size() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isShuffle) Collections.shuffle(list);
                else if (isClear && list != null) {
                    miniGame.getGameData().setList(name, null);
                    return frontBrace.getLastProc().run(miniGame, procUnit);
                } else {
                    if (isAdd) {
                        if (list == null) {
                            list = new ArrayList<>();
                            miniGame.getGameData().setList(name, list);
                        }
                        for (int i = 2; i < processList.size(); i += 2) {
                            String value = processList.get(i).run(miniGame, procUnit);
                            list.add(value);
                        }
                    } else if (isRemove) {
                        if (list == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                        String value = processList.get(2).run(miniGame, procUnit);
                        list.remove(value);
                    } else if (isGet) {
                        try {
                            String value = processList.get(2).run(miniGame, procUnit);
                            int index = Integer.parseInt(value);
                            if (list == null || list.isEmpty() || index >= list.size()) return "null" + frontBrace.getLastProc().run(miniGame, procUnit);
                            return list.get(FastMath.min(list.size() - 1, index)) + frontBrace.getLastProc().run(miniGame, procUnit);
                        } catch (Exception e) {
                            return frontBrace.getLastProc().run(miniGame, procUnit);
                        }
                    }
                }
            } else if (player != null) {
                java.util.List<String> list = GameStore.getPlayerData(player.getUniqueId()).getList(name);
                if (isClone) {
                    Process proc2 = processList.get(2);
                    String value2 = proc2.run(miniGame, procUnit);
                    if (proc2.getType() == ProcType.EXECUTE_GAME) miniGame.getGameData().setList(value2, new ArrayList<>(list));
                    else if (proc2.getType() == ProcType.EXECUTE_ONLINE) GameDataStore.getInst().setList(value2, new ArrayList<>(list));
                    else GameStore.getPlayerData(player.getUniqueId()).setList(value2, new ArrayList<>(list));
                } else if (isContains) return list == null ? "false" : list.contains(processList.get(2).run(miniGame, procUnit)) ? "true" : "false" + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isSize) return list == null ? "0" : list.size() + frontBrace.getLastProc().run(miniGame, procUnit);
                else if (isShuffle) Collections.shuffle(list);
                else if (isClear && list != null) {
                    GameStore.getPlayerData(player.getUniqueId()).setList(name, null);
                    return frontBrace.getLastProc().run(miniGame, procUnit);
                } else {
                    if (isAdd) {
                        if (list == null) {
                            list = new ArrayList<>();
                            GameStore.getPlayerData(player.getUniqueId()).setList(name, list);
                        }
                        for (int i = 2; i < processList.size(); i += 2) {
                            String value = processList.get(i).run(miniGame, procUnit);
                            list.add(value);
                        }
                    } else if (isRemove) {
                        if (list == null) return frontBrace.getLastProc().run(miniGame, procUnit);
                        String value = processList.get(2).run(miniGame, procUnit);
                        list.remove(value);
                    } else if (isGet) {
                        try {
                            String value = processList.get(2).run(miniGame, procUnit);
                            int index = Integer.parseInt(value);
                            if (list == null || list.isEmpty() || index >= list.size()) return "null" + frontBrace.getLastProc().run(miniGame, procUnit);
                            return list.get(FastMath.min(list.size() - 1, index)) + frontBrace.getLastProc().run(miniGame, procUnit);
                        } catch (Exception e) {
                            return frontBrace.getLastProc().run(miniGame, procUnit);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return frontBrace.getLastProc().run(miniGame, procUnit);
        }
        return frontBrace.getLastProc().run(miniGame, procUnit);
    }

}
