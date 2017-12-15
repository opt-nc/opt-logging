package nc.opt.core.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 2816ARN on 15/12/2017.
 */
@Configuration
public class TestConfig {
    @Bean
    LogMetierService logMetierService() {
        return new LogMetierService();
    }
}
