package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.proc.Process;

public interface MathOperator extends Process {

    void setValueA(Process process);

    void setValueB(Process process);

    default String parseIfInt(Number number) {
        if (number instanceof Integer) return String.valueOf(((int) number));
        else return String.valueOf(number);
    }

}
