package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.proc.data.control.FrontBrace;
import kr.jongwonlee.fmg.proc.data.control.If;

import java.util.ArrayList;
import java.util.List;

public class ParseUnit {

    private final List<FrontBrace> braceList;
    private List<If> ifList;
    private List<ProcType> executorList;

    public void addExecutor(ProcType procType) {
        if (!executorList.contains(procType)) executorList.add(procType);
    }

    public boolean useExecutor(ProcType procType) {
        if (executorList.contains(procType)) {
            executorList.remove(procType);
            return true;
        } else return false;
    }

    public void clearExecutor(ProcType procType) {
        executorList = new ArrayList<>();
    }

    public void removeBraceProc(FrontBrace frontBrace) {
        braceList.remove(frontBrace);
    }

    public void addBraceProc(FrontBrace frontBrace) {
        braceList.add(frontBrace);
    }

    public void removeBraceProc() {
        if (braceList.size() != 0) braceList.remove(braceList.size() - 1);
    }

    public boolean hasBrace() {
        return braceList.size() != 0;
    }

    public FrontBrace getFrontBrace() {
        if (braceList.size() == 0) return null;
        return braceList.get(braceList.size() - 1);
    }

    public void addIf(If anIf) {
        ifList.add(anIf);
    }

    public void removeIf() {
        if (ifList.size() != 0) ifList.remove(ifList.size() - 1);
    }

    public boolean hasIf() {
        return ifList.size() != 0;
    }

    public If getIf() {
        if (ifList.size() == 0) return null;
        return ifList.get(ifList.size() - 1);
    }

    @Deprecated
    public ParseUnit reset() {
        ifList = new ArrayList<>();
        return this;
    }

    public ParseUnit() {
        this.braceList = new ArrayList<>();
        this.ifList = new ArrayList<>();
        this.executorList = new ArrayList<>();
    }

}
