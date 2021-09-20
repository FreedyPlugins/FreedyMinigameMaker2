package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.proc.Process;

public interface MathOperator extends Process {

    double getCalculator(double numA, double numB);

    void setValueA(Process process);

    void setValueB(Process process);

    default String parseIfInt(Number number) {
        int intValue = number.intValue();
        if (intValue == number.doubleValue()) return String.valueOf(intValue);
        else return String.valueOf(number);
    }

    default String calculate(String string, String string2) {
        try {
            return parseIfInt(getCalculator(Double.parseDouble(string), Double.parseDouble(string2)));
        } catch (NumberFormatException e) {
            return string + string2;
        }
    }

}
