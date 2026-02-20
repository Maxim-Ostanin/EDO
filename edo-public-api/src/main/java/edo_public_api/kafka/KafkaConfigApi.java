package edo_public_api.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfigApi {

        @Bean
        public ProducerFactory<String, Object> producerFactory(KafkaProperties properties) {
            Map<String, Object> props = properties.buildProducerProperties(null);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(props);
        }

        @Bean
        public ConsumerFactory<String, Object> consumerFactory(KafkaProperties properties) {
            Map<String, Object> props = properties.buildConsumerProperties(null);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
            props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
            return new DefaultKafkaConsumerFactory<>(props);
        }

        @Bean
        public ConcurrentMessageListenerContainer<String, Object> replyContainer(
                ConsumerFactory<String, Object> consumerFactory) {
            ContainerProperties props = new ContainerProperties("approval-save-reply"); // укажи нужные топики
            props.setGroupId("api-reply-group");
            return new ConcurrentMessageListenerContainer<>(consumerFactory, props);
        }

        @Bean
        public ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate(
                ProducerFactory<String, Object> producerFactory,
                ConcurrentMessageListenerContainer<String, Object> replyContainer) {
            return new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
        }
    }
