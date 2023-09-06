package news.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoaderConfig {

    @Value("${buffer-articles.limit}")
    private Integer bufferLimit;

    @Value("${total-articles.limit}")
    private Integer downloadLimit;

    @Value("${threads-pool.size}")
    private Integer threadPoolSize;

    public Integer getBufferLimit() {
        return bufferLimit;
    }

    public Integer getDownloadLimit() {
        return downloadLimit;
    }

    public Integer getThreadPoolSize() {
        return threadPoolSize;
    }
}
