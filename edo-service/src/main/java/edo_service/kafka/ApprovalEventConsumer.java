package edo_service.kafka;

import common.dto.event.ApprovalEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalEventConsumer {

    @KafkaListener(topics = "${kafka.topic.approval-events}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consumeApprovalEvent(ApprovalEventDto event) {
        log.info("Получено событие из Kafka: {}", event);

        // Здесь добавьте бизнес-логику обработки события
        switch (event.getEventType()) {
            case "CREATED":
                log.info("Новое согласование создано с id: {}", event.getApprovalId());
                break;
            case "UPDATED":
                log.info("Согласование обновлено с id: {}", event.getApprovalId());
                break;
            case "DELETED":
                log.info("Согласование удалено с id: {}", event.getApprovalId());
                break;
        }
    }
}
