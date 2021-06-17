package com.myproject.myweb.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

// 멀티 쓰레드
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    private final static int POOL_SIZE = 10;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        // threadPoolTaskScheduler.setThreadNamePrefix("hello-");
        threadPoolTaskScheduler.initialize(); // bean 초기화

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}