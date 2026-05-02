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
public class AdditionalApprovalService {


    public final AdditionalApprovalRepository additionalApprovalRepository;
    private final ApprovalRepository approvalRepository;
    private final AdditionalApprovalToAdditionalApprovalDtoConverter converter;
    private final AdditionalApprovalValidator validator;

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
    public AdditionalApprovalDto getAll() {
        AdditionalApproval entity = (AdditionalApproval) additionalApprovalRepository.findAll();
        return converter.toDto(entity);
    }

    //FIXME: Ты в контроллере вызываешь все эти методы которые идут ниже. Но они пустые, вообще не обращаются к репозиторию и ничего не меняют в БД
    //FIXME:А методы которые идут выше написаны корректно, вызывают репозиторий, производят валидации и конвертации в общем все что нужно, почему ты не вызываешь их?
    //FIXME: Вызывай методы выше, методы ниже все удаляй. Если нужно просто переименуй методы выше

}

