package kr.jongwonlee.fmg.proc.data.minecraft;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;
import net.jafama.FastMath;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@Processable(alias = "health")
public class Health implements Process {

    Process process;
    boolean isSet;
    boolean isAdd;
    boolean isSize;
    boolean isEntity;

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        isAdd = parseUnit.useExecutor(ProcType.EXECUTE_ADD);
        isSet = parseUnit.useExecutor(ProcType.EXECUTE_SET);
        isSize = parseUnit.useExecutor(ProcType.EXECUTE_SIZE);
        isEntity = parseUnit.useExecutor(ProcType.EXECUTE_ENTITY);
        process = FileParser.parseProcess(parseUnit, arguments);
    }

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        Player player = procUnit.target.player;
        Entity entity = procUnit.target.entity;
        String value = process.run(miniGame, procUnit);
        try {
            if (isEntity && entity instanceof LivingEntity) {
                LivingEntity livingEntity = ((LivingEntity) entity);
                if (isSize) {
                    if (isAdd) {
                        double maxHealth = livingEntity.getMaxHealth();
                        double amount = Double.parseDouble(value);
                        livingEntity.setHealth(FastMath.max(0, maxHealth + amount));
                    } else if (isSet) {
                        double amount = Double.parseDouble(value);
                        livingEntity.setMaxHealth(FastMath.max(0, amount));
                    } else {
                        return livingEntity.getMaxHealth() + value;
                    }
                } else {
                    if (isAdd) {
                        double health = livingEntity.getHealth();
                        double amount = Double.parseDouble(value);
                        livingEntity.setHealth(FastMath.max(0, FastMath.min(livingEntity.getMaxHealth(), health + amount)));
                    } else if (isSet) {
                        double amount = Double.parseDouble(value);
                        livingEntity.setHealth(FastMath.max(0, FastMath.min(livingEntity.getMaxHealth(), amount)));
                    } else {
                        return livingEntity.getHealth() + value;
                    }
                }
            } else {
                if (isSize) {
                    if (isAdd) {
                        double maxHealth = player.getMaxHealth();
                        double amount = Double.parseDouble(value);
                        player.setHealth(FastMath.max(0, maxHealth + amount));
                    } else if (isSet) {
                        double amount = Double.parseDouble(value);
                        player.setMaxHealth(FastMath.max(0, amount));
                    } else {
                        return player.getMaxHealth() + value;
                    }
                } else {
                    if (isAdd) {
                        double health = player.getHealth();
                        double amount = Double.parseDouble(value);
                        player.setHealth(FastMath.max(0, FastMath.min(player.getMaxHealth(), health + amount)));
                    } else if (isSet) {
                        double amount = Double.parseDouble(value);
                        player.setHealth(FastMath.max(0, FastMath.min(player.getMaxHealth(), amount)));
                    } else {
                        return player.getHealth() + value;
                    }
                }
            }
        } catch (Exception ignored) {
            return value;
        }
        return value;
    }

    @Override
    public ProcType getType() {
        return ProcType.HEALTH;
    }
}
