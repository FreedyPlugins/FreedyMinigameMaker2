package kr.jongwonlee.fmg.process;

import kr.jongwonlee.fmg.process.data.control.*;
import kr.jongwonlee.fmg.process.data.etc.*;
import kr.jongwonlee.fmg.process.data.minecraft.Broadcast;
import kr.jongwonlee.fmg.process.data.minecraft.SendMessage;
import kr.jongwonlee.fmg.util.GameAlert;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ProcType {
    /*control package---------------------------------*/
    EXECUTE_GAME(ExecuteGame.class),
    EXECUTE_ONLINE(ExecuteOnline.class),
    EXECUTE_PLAYER(ExecutePlayer.class),
    EXECUTE_SET(ExecuteSet.class),
    IF(If.class),
    IF_BIG(IfBig.class),
    IF_BIG_SAME(IfBigSame.class),
    IF_EQUAL(IfEqual.class),
    IF_NOT_EQUAL(IfNotEqual.class),
    IF_SMALL(IfSmall.class),
    IF_SMALL_SAME(IfSmallSame.class),
    SMALL_END_BRACE(SmallEndBrace.class),
    SMALL_FRONT_BRACE(SmallFrontBrace.class),
    MID_END_BRACE(MidEndBrace.class),
    MID_FRONT_BRACE(MidFrontBrace.class),
    /*etc package---------------------------------*/
    DATA(Data.class),
    DO(Do.class),
    EXECUTE(Execute.class),
    LOG(Log.class),
    NAME(Name.class),
    NOTHING(Nothing.class),
    TIMINGS(Timings.class),
    /*minecraft package---------------------------------*/
    BROADCAST(Broadcast.class),
    SEND_MESSAGE(SendMessage.class),
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

