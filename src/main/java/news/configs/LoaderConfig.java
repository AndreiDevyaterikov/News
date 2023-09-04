package news.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoaderConfig {

    @Value("${buffer-articles.limit}")
    private String bufferLimit;

    @Value("${total-articles.limit}")
    private String downloadLimit;

    @Value("${threads-pool.size}")
    private String threadPoolSize;

    public Integer getBufferLimit() {
        return Integer.parseInt(bufferLimit);
    }

    public Integer getDownloadLimit() {
        return Integer.parseInt(downloadLimit);
    }

    public Integer getThreadPoolSize() {
        return Integer.parseInt(threadPoolSize);
    }
}
