package news.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class ExecutorConfig {

    private final LoaderConfig loaderConfig;

    @Bean(name = "taskPoolConfig")
    public ThreadPoolTaskExecutor taskExecutor() {

        var threadPoolSize = loaderConfig.getThreadPoolSize();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSize);
        executor.setMaxPoolSize(threadPoolSize + 2);
        executor.setThreadNamePrefix("thread-");
        executor.initialize();
        return executor;
    }
}
