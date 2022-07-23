package com.gt.datafetcher.gtdatafetcher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    //Configuring Asynchronous method handling in Spring Boot
    @Value("${custom.concurrency.async.config.core_pool_size}")
    private String corePoolSize;
    @Value("${custom.concurrency.async.config.max_pool_size}")
    private String maxPoolSize;


    @Bean(name = "threadPoolTaskExecutor")
    public Executor getThreadPoolTaskExecutor(){
        int core_size = Integer.parseInt(corePoolSize);
        int max_size = Integer.parseInt(maxPoolSize);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(core_size);
        executor.setMaxPoolSize(max_size);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async_Method_Pool");
        return executor;
    }
}
