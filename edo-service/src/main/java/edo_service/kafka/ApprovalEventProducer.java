package edo_service.kafka;

import common.dto.event.ApprovalEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.approval-events}")
    private String approvalTopic;

    public void sendApprovalEvent(ApprovalEventDto event) {
        log.info("Отправка события в Kafka: {}", event);
        kafkaTemplate.send(approvalTopic, event.getApprovalId().toString(), event);
    }
}