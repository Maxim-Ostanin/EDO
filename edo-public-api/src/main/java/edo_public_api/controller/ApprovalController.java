package edo_public_api.controller;

import common.dto.ApprovalDto;
import edo_service.exception.ApprovalValidationException;
import edo_service.service.ApprovalService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/approvals")
public class ApprovalController {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalController.class);
    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping
    public ResponseEntity<List<ApprovalDto>> getApprovals() {
        logger.info("Получение списка согласований");
        List<ApprovalDto> approvals = approvalService.getAllApprovals();
        return ResponseEntity.ok(approvals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovalDto> getApprovalById(@PathVariable Long id) {
        logger.info("Запрос на получение согласований с id: {}", id);
        ApprovalDto approvalDto = approvalService.getApprovalById(id);
        return ResponseEntity.ok(approvalDto);
    }

    @PostMapping
    public ResponseEntity<ApprovalDto> createApproval(@Valid @RequestBody ApprovalDto approvalDto) {
        logger.info("Запрос на создание согласования: {}", approvalDto);
        try {
            ApprovalDto savedApproval = approvalService.createApproval(approvalDto);
            logger.info("Согласование успешно создано: {}", savedApproval);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedApproval);
        } catch (ApprovalValidationException e) {
            logger.error("Ошибка валидации согласования: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Ошибка при создании согласования: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApprovalDto> updateApproval(@PathVariable Long id, @Valid @RequestBody ApprovalDto approvalDto) {
        logger.info("Запрос на обновление согласования с id: {}", id);

        try {
            ApprovalDto updatedApproval = approvalService.updateApproval(id, approvalDto);
            logger.info("Согласование успешно обновлено: {}", updatedApproval);
            return ResponseEntity.ok(updatedApproval);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении согласования с id: {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApproval(@PathVariable Long id) {
        logger.info("Запрос на удаление согласования с id: {}", id);

        try {
            approvalService.deleteApproval(id);
            logger.info("Согласование успешно удалено: id {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Ошибка при удалении согласования с id: {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
