package kr.jongwonlee.fmg.util;

import kr.jongwonlee.fmg.process.ProcType;

import java.util.ArrayList;
import java.util.List;

public class ParseUnit {

    public List<ProcType> procTypeList;
    private final List<ProcType> smallBraceList;
    private String args;

    public ProcType getSmallBraceProc() {
        if (smallBraceList.size() == 0) return null;
        return smallBraceList.get(smallBraceList.size() - 1);
    }

    public void addSmallBraceProc(ProcType procType) {
        smallBraceList.add(procType);
    }

    public void removeSmallBraceProc() {
        if (smallBraceList.size() != 0) smallBraceList.remove(smallBraceList.size() - 1);
    }

    public ProcType getProcType(int index) {
        int finalIndex = procTypeList.size() - index - 1;
        if (finalIndex == -1 || procTypeList.size() <= finalIndex) return null;
        else return procTypeList.get(finalIndex);
    }


    public void putArgs(String args) {
        this.args = args;
    }

    public String getArgs() {
        return this.args;
    }

    public ParseUnit() {
        this.procTypeList = new ArrayList<>();
        this.smallBraceList = new ArrayList<>();
    }

    public void addProcType(ProcType procType) {
        procTypeList.add(procType);
    }

}
