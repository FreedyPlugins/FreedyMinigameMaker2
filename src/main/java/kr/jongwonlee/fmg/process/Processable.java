package kr.jongwonlee.fmg.process;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface Processable {
    String[] alias();
}
