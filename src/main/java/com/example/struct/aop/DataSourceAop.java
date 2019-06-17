package com.example.struct.aop;

import com.example.struct.bean.DBContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class DataSourceAop {
    @Pointcut("!@annotation(com.example.struct.annotation.Master)" +
            "&&(execution(* com.example.struct.service..*.select*(..))" +
            "|| execution(* com.example.struct.service..*.get*(..)))")
    public void readPointcut() {}

    @Pointcut("@annotation(com.example.struct.annotation.Master) " +
            "|| execution(* com.example.struct.service..*.insert*(..)) " +
            "|| execution(* com.example.struct.service..*.add*(..)) " +
            "|| execution(* com.example.struct.service..*.update*(..)) " +
            "|| execution(* com.example.struct.service..*.edit*(..)) " +
            "|| execution(* com.example.struct.service..*.delete*(..)) " +
            "|| execution(* com.example.struct.service..*.remove*(..))")
    public void writePointcut() {}

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }
}
