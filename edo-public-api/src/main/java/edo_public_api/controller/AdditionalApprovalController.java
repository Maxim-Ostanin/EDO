package edo_public_api.controller;


import common.dto.AdditionalApprovalDto;
import edo_service.exception.ApprovalValidationException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edo_service.service.AdditionalApprovalService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/additional-approvals")
public class AdditionalApprovalController {

    private static final Logger logger = LoggerFactory.getLogger(AdditionalApprovalController.class);
    private final AdditionalApprovalService additionalApprovalService;

    public AdditionalApprovalController(AdditionalApprovalService additionalApprovalService) {
        this.additionalApprovalService = additionalApprovalService;
    }


    // 1. ПОЛУЧЕНИЕ ВСЕХ (GET ALL)
    @GetMapping
    public ResponseEntity<List<AdditionalApprovalDto>> getAllAdditionalApprovals() {
        List<AdditionalApprovalDto> approvals = additionalApprovalService.getAll();
        return ResponseEntity.ok(approvals);
    }


    // 4. СОЗДАНИЕ (CREATE)
    @PostMapping
    public ResponseEntity<AdditionalApprovalDto> create(
            @Valid @RequestBody AdditionalApprovalDto createDto) {
        logger.info("Запрос на создание дополнительного согласования: {}", createDto);

        try {
            AdditionalApprovalDto savedAdditionalApproval = additionalApprovalService.create(createDto);
            logger.info("Дополнительное согласование успешно создано: {}", savedAdditionalApproval);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAdditionalApproval);
        } catch (ApprovalValidationException e) {
            logger.error("Ошибка валидации дополнительного согласования: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Ошибка при создании дополнительного согласования: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 5. ОБНОВЛЕНИЕ (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<AdditionalApprovalDto> updateAdditionalApproval(
            @PathVariable Long id,
            @Valid @RequestBody AdditionalApprovalDto updateDto) {
        updateDto.setId(id);
        AdditionalApprovalDto updated = additionalApprovalService.update(updateDto);

        logger.info("Запрос на обновление дополнительного согласования с id: {}", id);

        try {
            AdditionalApprovalDto updatedAdditionalApproval = additionalApprovalService.update(updateDto);
            logger.info("Дополнительное согласование успешно обновлено: {}", updatedAdditionalApproval);
            return ResponseEntity.ok(updatedAdditionalApproval);
        } catch (ApprovalValidationException e) {
            logger.error("Ошибка валидации при обновлении дополнительного согласования с id: {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении дополнительного согласования с id: {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 6. УДАЛЕНИЕ (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdditionalApproval(@PathVariable Long id) {
        logger.info("Запрос на удаление дополнительного согласования с id: {}", id);

        try {
            additionalApprovalService.delete(id);
            logger.info("Дополнительное согласование успешно удалено: id {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Ошибка при удалении дополнительного согласования с id: {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}