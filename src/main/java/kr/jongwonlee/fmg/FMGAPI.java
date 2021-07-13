package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.Process;

public class FMGAPI {

    public static void addExternalProc(Class<? extends Process> procClass) {
        ProcType.addExternalProc(procClass);
    }

    public static void removeExternalProc(Class<? extends Process> procClass) {
        ProcType.removeExternalProc(procClass);
    }

    public static java.util.List<Class<? extends Process>> getExternalProc() {
        return ProcType.getExternalProc();
    }

}
