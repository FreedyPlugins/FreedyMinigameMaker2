package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.proc.data.control.*;
import kr.jongwonlee.fmg.proc.data.etc.*;
import kr.jongwonlee.fmg.proc.data.minecraft.*;
import kr.jongwonlee.fmg.util.GameAlert;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ProcType {
    /*control package---------------------------------*/
    ADD(Add.class),
    AND(And.class),
    DIVIDE(Divide.class),
    ELSE(Else.class),
    EXECUTE_ADD(ExecuteAdd.class),
    EXECUTE_GAME(ExecuteGame.class),
    EXECUTE_ONLINE(ExecuteOnline.class),
    EXECUTE_REMOVE(ExecuteRemove.class),
    EXECUTE_SET(ExecuteSet.class),
    IF(If.class),
    IF_BIG(IfBig.class),
    IF_BIG_SAME(IfBigSame.class),
    IF_EQUAL(IfEqual.class),
    IF_NOT_EQUAL(IfNotEqual.class),
    IF_SMALL(IfSmall.class),
    IF_SMALL_SAME(IfSmallSame.class),
    MID_END_BRACE(MidEndBrace.class),
    MID_FRONT_BRACE(MidFrontBrace.class),
    MULTIPLY(Multiply.class),
    NOTHING(Nothing.class),
    PLAYER(Player.class),
    OR(Or.class),
    REMAINDER(Remainder.class),
    RETURN(Return.class),
    SMALL_END_BRACE(SmallEndBrace.class),
    SMALL_FRONT_BRACE(SmallFrontBrace.class),
    SUBTRACT(Subtract.class),
    THEN(Then.class),
    WHILE(While.class),
    /*etc package---------------------------------*/
    DATA(Data.class),
    DELAY(Delay.class),
    DO(Do.class),
    EXECUTE(Execute.class),
    EXECUTE_ASYNC(ExecuteAsync.class),
    EXECUTE_CLEAR(ExecuteClear.class),
    EXECUTE_GET(ExecuteGet.class),
    EXECUTE_LORE(ExecuteLore.class),
    EXECUTE_SIZE(ExecuteSize.class),
    EXECUTE_TYPE(ExecuteType.class),
    INT(Int.class),
    LIST(List.class),
    LOG(Log.class),
    NAME(Name.class),
    RANDOM(Random.class),
    TIMINGS(Timings.class),
    UUID(Uuid.class),
    /*minecraft package---------------------------------*/
    ACTION_BAR(ActionBar.class),
    BLOCK(Block.class),
    BROADCAST(Broadcast.class),
    INVENTORY(Inventory.class),
    ITEM(Item.class),
    LOCATION(Location.class),
    POTION(Potion.class),
    SEND_MESSAGE(SendMessage.class),
    SOUND(Sound.class),
    TELEPORT(Teleport.class),
    TITLE(Title.class),
    /*---------------------------------*/
    ;

    private final Class<? extends Process> aClass;

    ProcType(Class<? extends Process> aClass) {
        this.aClass = aClass;
    }

    public Process getNewProcess() {
        try {
            return aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            GameAlert.INVALID_PROCESS_PARSE.print(new String[]{name()});
            return new Nothing();
        }
    }

    public static ProcType getProcType(String name) {
        try {
            return Arrays.stream(ProcType.values()).filter(processType -> {
                try {
                    Processable annotation = processType.aClass.getAnnotation(Processable.class);
                    return Arrays.asList(annotation.alias()).contains(name);
                } catch (Exception e) {
                    return false;
                }
            }).collect(Collectors.toList()).get(0);
        } catch (Exception e) {
            return null;
        }
    }

}

