package kr.jongwonlee.fmg;

import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.Process;
import org.bukkit.plugin.java.JavaPlugin;

public class FMGAPI {

    /**
     * add external process.  Process type must be ProcType.EXTERNAL
     */
    public static void addExternalProc(Class<? extends Process> procClass) {
        ProcType.addExternalProc(procClass);
    }

    /**
     * remove external process.  Process type must be ProcType.EXTERNAL
     */
    public static void removeExternalProc(Class<? extends Process> procClass) {
        ProcType.removeExternalProc(procClass);
    }

    /**
     * get external processes.  Process type must be ProcType.EXTERNAL
     */
    public static java.util.List<Class<? extends Process>> getExternalProc() {
        return ProcType.getExternalProc();
    }

}
