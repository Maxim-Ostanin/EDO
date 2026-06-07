package edo_service.kafka;

import common.dto.event.AdditionalApprovalEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdditionalApprovalEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.additional-approval-events}")
    private String additionalApprovalTopic;

    public void sendAdditionalApprovalEvent(AdditionalApprovalEventDto event) {
        log.info("Отправка события в Kafka: {}", event);
        kafkaTemplate.send(additionalApprovalTopic, event.getAdditionalApprovalId().toString(), event);
    }
}