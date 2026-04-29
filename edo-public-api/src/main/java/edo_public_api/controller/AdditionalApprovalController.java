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

import java.util.List;

@RestController
@RequestMapping("/additional-approvals")
public class AdditionalApprovalController {

    private static final Logger logger = LoggerFactory.getLogger(AdditionalApprovalController.class);
    private final AdditionalApprovalService additionalApprovalService;

    public AdditionalApprovalController(AdditionalApprovalService additionalApprovalService) {
        this.additionalApprovalService = additionalApprovalService;
    }

    //FIXME: В т.з. было обозначенно 4 метода, из них на получение 1. У тебя на получение 3.
    // В целом они логичные но их не было в т.з. а значит фронт не будет отправлять в них запросы и они будут не задействованы.
    // Стараемся делать все по т.з. если кажется что нужно что то еще лучше уточнить у того кто дал т.з.

    // 1. ПОЛУЧЕНИЕ ВСЕХ (GET ALL)
    @GetMapping
    public ResponseEntity<List<AdditionalApprovalDto>> getAllAdditionalApprovals() {
        logger.info("Получение списка дополнительных согласований");
        List<AdditionalApprovalDto> approvals = additionalApprovalService.getAllAdditionalApprovals();
        return ResponseEntity.ok(approvals);
    }

    // 2. ПОЛУЧЕНИЕ ПО ID (GET BY ID)
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalApprovalDto> getAdditionalApprovalById(@PathVariable Long id) {
        logger.info("Запрос на получение дополнительного согласования с id: {}", id);
        AdditionalApprovalDto additionalApprovalDto = additionalApprovalService.getAdditionalApprovalById(id);
        return ResponseEntity.ok(additionalApprovalDto);
    }

    // 3. ПОЛУЧЕНИЕ ПО APPROVAL ID (GET BY APPROVAL ID)
    @GetMapping("/by-approval/{approvalId}")
    public ResponseEntity<AdditionalApprovalDto> getAdditionalApprovalByApprovalId(@PathVariable Long approvalId) {
        logger.info("Запрос на получение дополнительного согласования по approvalId: {}", approvalId);
        AdditionalApprovalDto additionalApprovalDto = additionalApprovalService.getAdditionalApprovalByApprovalId(approvalId);
        return ResponseEntity.ok(additionalApprovalDto);
    }

    // 4. СОЗДАНИЕ (CREATE)
    @PostMapping
    public ResponseEntity<AdditionalApprovalDto> createAdditionalApproval(
            @Valid @RequestBody AdditionalApprovalDto createDto) {
        logger.info("Запрос на создание дополнительного согласования: {}", createDto);

        try {
            AdditionalApprovalDto savedAdditionalApproval = additionalApprovalService.AdditionalApproval(createDto);
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
        logger.info("Запрос на обновление дополнительного согласования с id: {}", id);

        try {
            AdditionalApprovalDto updatedAdditionalApproval = additionalApprovalService.updateAdditionalApproval(id, updateDto);
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
            additionalApprovalService.deleteAdditionalApproval(id);
            logger.info("Дополнительное согласование успешно удалено: id {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Ошибка при удалении дополнительного согласования с id: {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}