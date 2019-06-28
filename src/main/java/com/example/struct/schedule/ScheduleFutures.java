package com.example.struct.schedule;

import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

/**
 * @author zhouchao
 */
@Component
public class ScheduleFutures {
    private static ScheduledFuture dynamicTaskFuture1;
    private static ScheduledFuture dynamicTaskFuture2;

    public ScheduledFuture getDynamicTaskFuture1() {
        return dynamicTaskFuture1;
    }

    public void setDynamicTaskFuture1(ScheduledFuture dynamicTaskFuture1) {
        ScheduleFutures.dynamicTaskFuture1 = dynamicTaskFuture1;
    }

    public ScheduledFuture getDynamicTaskFuture2() {
        return dynamicTaskFuture2;
    }

    public void setDynamicTaskFuture2(ScheduledFuture dynamicTaskFuture2) {
        ScheduleFutures.dynamicTaskFuture2 = dynamicTaskFuture2;
    }
}
