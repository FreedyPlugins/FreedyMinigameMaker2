package kr.jongwonlee.fmg.proc;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface Processable {
    String[] alias();
}
