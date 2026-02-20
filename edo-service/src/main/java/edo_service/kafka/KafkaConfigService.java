package edo_service.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableKafka
public class KafkaConfigService {

            @Bean
            public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
                    ConsumerFactory<String, Object> consumerFactory,
                    KafkaTemplate<String, Object> kafkaTemplate) {
                ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                        new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(consumerFactory);
                factory.setReplyTemplate(kafkaTemplate);
                return factory;
            }
        }