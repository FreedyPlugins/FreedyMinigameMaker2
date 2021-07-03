package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.proc.ProcType;
import kr.jongwonlee.fmg.proc.Process;

public abstract class ConditionOperator implements Process {

    static double toDouble(String string) throws NumberFormatException {
        return Double.parseDouble(string);
    }

    static boolean getValue(String valueA, String valueB, ProcType operator) {
        try {
            switch (operator) {
                case IF_BIG: return toDouble(valueA) > toDouble(valueB);
                case IF_BIG_SAME: return toDouble(valueA) >= toDouble(valueB);
                case IF_EQUAL: return valueA.equals(valueB);
                case IF_NOT_EQUAL: return !valueA.equals(valueB);
                case IF_SMALL: return toDouble(valueA) < toDouble(valueB);
                case IF_SMALL_SAME: return toDouble(valueA) <= toDouble(valueB);
                default: return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
