package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.proc.Process;

public interface MathOperator extends Process {

    void setValueA(Process process);

    void setValueB(Process process);

    default String parseIfInt(Number number) {
        int intValue = number.intValue();
        if (intValue == number.doubleValue()) return String.valueOf(intValue);
        else return String.valueOf(number);
    }

}
