package kr.jongwonlee.fmg.parse;

import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.data.control.FrontBrace;

import java.util.ArrayList;
import java.util.List;

public class ParseUnit {

    public List<ProcType> procTypeList;
    private final List<FrontBrace> braceList;
    private String args;

    public void addBraceProc(FrontBrace frontBrace) {
        braceList.add(frontBrace);
    }

    public void removeBraceProc() {
        if (braceList.size() != 0) braceList.remove(braceList.size() - 1);
    }

    public FrontBrace getFrontBrace() {
        if (braceList.size() == 0) return null;
        return braceList.get(braceList.size() - 1);
    }

    public boolean hasBrace() {
        return braceList.size() != 0;
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
        this.braceList = new ArrayList<>();
    }

    public void addProcType(ProcType procType) {
        procTypeList.add(procType);
    }

}
