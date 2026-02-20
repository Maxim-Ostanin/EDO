package edo_service.kafka;

import common.dto.AdditionalApprovalDto;
import common.dto.ApprovalDto;
import common.dto.EditRequestKafka.EditAdditionalApprovalRequest;
import common.dto.EditRequestKafka.EditApprovalRequest;
import edo_service.service.AdditionalApprovalService;
import edo_service.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceConsumer {

    private final AdditionalApprovalService additionalApprovalService;
    private final ApprovalService approvalService;

    @KafkaListener(topics = "additional-approval-save-request")
    public void addAppSave(AdditionalApprovalDto dto) {
        log.info("Получено событие на сохранение доп.согл. для основного согл. с ID: {}", dto.getApprovalId());
        additionalApprovalService.saveAddApproval(dto);
    }

    @KafkaListener(topics = "additional-approval-delete-request")
    public void addAppDelete(Long id) {
        log.info("Получено событие на удаление ID: {}", id);
        additionalApprovalService.deleteById(id);
    }

    @KafkaListener(topics = "additional-approval-get-request")
    @SendTo
    public AdditionalApprovalDto addAppGet(Long id) {
        log.info("Получено событие на получение доп.согл. с ID: {}", id);
        return additionalApprovalService.findById(id);
    }

    @KafkaListener(topics = "additional-approval-edit-request")
    @SendTo
    public AdditionalApprovalDto addAppEdit(EditAdditionalApprovalRequest request) {
        log.info("Получено событие на изменение доп.согл. с ID: {}", request.getId());
        return additionalApprovalService.editAddApproval(request.getId(), request.getDto());
    }

    /*--------------------------------------------------------*/

    @KafkaListener(topics = "approval-get-all-request")
    @SendTo
    public List<ApprovalDto> appGetAll(Object ignored) {
        log.info("Получено событие на получение всех согласований");
        return approvalService.getAllApprovals();
    }

    @KafkaListener(topics = "approval-save-request")
    @SendTo
    public ApprovalDto appSave(ApprovalDto dto) {
        log.info("Получено событие на сохранение основного согл. с ID: {}", dto.getId());
        return approvalService.createApproval(dto);
    }

    @KafkaListener(topics = "approval-delete-request")
    public void appDelete(Long id) {
        log.info("Получено событие на удаление ID: {}", id);
        approvalService.deleteApproval(id);
    }

    @KafkaListener(topics = "approval-get-request")
    @SendTo
    public ApprovalDto appGet(Long id) {
        log.info("Получено событие на получение доп.согл. с ID: {}", id);
        return approvalService.getApprovalById(id);
    }

    @KafkaListener(topics = "approval-edit-request")
    @SendTo
    public ApprovalDto appEdit(EditApprovalRequest request) {
        log.info("Получено событие на изменение доп.согл. с ID: {}", request.getId());
        return approvalService.updateApproval(request.getId(), request.getDto());
    }


}


