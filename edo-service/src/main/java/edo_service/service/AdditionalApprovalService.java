package edo_service.service;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import edo_service.converter.AdditionalApprovalToAdditionalApprovalDtoConverter;
import edo_service.AdditionalApprovalValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor  // ← Lombok создаст конструктор со всеми final полями
public class AdditionalApprovalService {

    // Все зависимости перечисляем как final поля
    private final AdditionalApprovalRepository additionalApprovalRepository;
    private final ApprovalRepository approvalRepository;
    private final AdditionalApprovalToAdditionalApprovalDtoConverter converter;
    private final AdditionalApprovalValidator validator;  // ← ИНЖЕКТ ВАЛИДАТОРА

    public AdditionalApprovalService(AdditionalApprovalRepository additionalApprovalRepository, ApprovalRepository approvalRepository, AdditionalApprovalToAdditionalApprovalDtoConverter converter, AdditionalApprovalValidator validator) {
        this.additionalApprovalRepository = additionalApprovalRepository;
        this.approvalRepository = approvalRepository;
        this.converter = converter;
        this.validator = validator;
    }

    @Transactional
    public AdditionalApprovalDto create(AdditionalApprovalDto dto) {
        // ИСПОЛЬЗУЕМ ВАЛИДАТОР
        validator.validateAll(dto);

        // Конвертация
        AdditionalApproval entity = converter.toEntity(dto);

        // Установка связей
        if (dto.getApprovalId() != null) {
            Approval approval = approvalRepository.findById(dto.getApprovalId())
                    .orElseThrow(() -> new RuntimeException("Approval not found"));
            entity.setApproval(approval);
        }

        // Сохранение
        AdditionalApproval savedEntity = additionalApprovalRepository.save(entity);

        return converter.toDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public AdditionalApprovalDto getById(Long id) {
        AdditionalApproval entity = additionalApprovalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return converter.toDto(entity);
    }

    public void deleteAdditionalApproval(Long id) {
    }

    public AdditionalApprovalDto getAdditionalApprovalByApprovalId(Long approvalId) {
        return null;
    }

    public AdditionalApprovalDto updateAdditionalApproval(Long id, @Valid AdditionalApprovalDto updateDto) {
        return updateDto;
    }

    public AdditionalApprovalDto AdditionalApproval(@Valid AdditionalApprovalDto createDto) {
        return createDto;
    }

    public AdditionalApprovalDto getAdditionalApprovalById(Long id) {
        return null;
    }

    public List<AdditionalApprovalDto> getAllAdditionalApprovals() {
        return List.of();
    }
}

