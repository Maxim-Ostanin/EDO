package common.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        // durable = true означает, что очередь будет сохраняться после перезапуска RabbitMQ
        boolean durable = true;
        // exclusive = false означает, что очередь может использоваться другими соединениями
        boolean exclusive = false;
        // autoDelete = false означает, что очередь не будет удалена автоматически, если она используется
        boolean autoDelete = false;

        return new Queue("queue.name", durable, exclusive, autoDelete);
    }
}
