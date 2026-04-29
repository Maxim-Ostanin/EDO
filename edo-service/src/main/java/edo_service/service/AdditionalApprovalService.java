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
//FIXME: у меня на аннотации @RequiredArgsConstructor компилятор ругается, вроде из за того что у тебя уже реализован конструктор с аргументами ниже
@Service
@RequiredArgsConstructor  // ← Lombok создаст конструктор со всеми final полями //FIXME:Такие комментарии не нужно пушить, это ИИ обьясняет тебе что он сделал.
//FIXME: Да и в принципе комментарии нигде не нужны, если только прям очень сложное место в котором нужно что то пояснить для будущих покалений разработчиков но такое прям редко быват
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

    //FIXME: Ты в контроллере вызываешь все эти методы которые идут ниже. Но они пустые, вообще не обращаются к репозиторию и ничего не меняют в БД
    //FIXME:А методы которые идут выше написаны корректно, вызывают репозиторий, производят валидации и конвертации в общем все что нужно, почему ты не вызываешь их?
    //FIXME: Вызывай методы выше, методы ниже все удаляй. Если нужно просто переименуй методы выше

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

