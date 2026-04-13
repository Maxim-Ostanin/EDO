package edo_service.service;

import common.dto.AdditionalApprovalDto;
import common.dto.AdditionalApprovalCreateDto;
import common.dto.AdditionalApprovalUpdateDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdditionalApprovalService {

    // 1. ПОЛУЧЕНИЕ ВСЕХ (GET ALL)
    public List<AdditionalApprovalDto> getAllAdditionalApprovals() {
        return List.of(
                new AdditionalApprovalDto(1L, 100L, "MANAGER", "PENDING", "Ждет согласования", null),
                new AdditionalApprovalDto(2L, 101L, "LEGAL", "APPROVED", "Согласовано юристом", LocalDateTime.now())
        );
    }

    // 2. ПОЛУЧЕНИЕ ПО ID (GET BY ID)
    public AdditionalApprovalDto getAdditionalApprovalById(Long id) {
        return new AdditionalApprovalDto(id, 100L, "MANAGER", "PENDING", "Ждет согласования", null);
    }

    // 3. ПОЛУЧЕНИЕ ПО APPROVAL ID (GET BY APPROVAL ID)
    public AdditionalApprovalDto getAdditionalApprovalByApprovalId(Long approvalId) {
        return new AdditionalApprovalDto(1L, approvalId, "MANAGER", "PENDING", "Ждет согласования", null);
    }

    // 4. СОЗДАНИЕ (CREATE)
    public AdditionalApprovalDto createAdditionalApproval(AdditionalApprovalCreateDto createDto) {
        return new AdditionalApprovalDto(
                1L,                           // id (генерируется автоматически)
                createDto.getApprovalId(),    // approvalId из запроса
                createDto.getType(),          // type из запроса
                createDto.getStatus(),        // status из запроса
                createDto.getComment(),       // comment из запроса
                null                          // responseDate (пока null)
        );
    }

    // 5. ОБНОВЛЕНИЕ (UPDATE)
    public AdditionalApprovalDto updateAdditionalApproval(Long id, AdditionalApprovalUpdateDto updateDto) {
        return new AdditionalApprovalDto(
                id,                           // id из запроса
                100L,                         // approvalId (существующий)
                updateDto.getType(),          // type из запроса
                updateDto.getStatus(),        // status из запроса
                updateDto.getComment(),       // comment из запроса
                updateDto.getResponseDate()   // responseDate из запроса
        );
    }

    // 6. УДАЛЕНИЕ (DELETE)
    public void deleteAdditionalApproval(Long id) {
        // Логика удаления
        System.out.println("AdditionalApproval с id " + id + " удален");
    }
}