package news.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class TaskPoolConfig {
    @Bean
    public Executor taskExecutor() {
        // Используем инкапсулированный в Spring пул асинхронных потоков
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize (10); // Инициализируем количество потоков
        executor.setMaxPoolSize (20); // Максимальное количество потоков
        executor.setQueueCapacity (200); // буферная очередь
        executor.setKeepAliveSeconds (60); // Разрешить время простоя в секунду
        executor.setThreadNamePrefix ("taskExecutor -"); // Префикс имени пула потоков - удобный поиск журнала
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize (); // Инициализируем
        return executor;
    }
}
