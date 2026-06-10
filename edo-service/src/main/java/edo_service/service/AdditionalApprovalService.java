package edo_service.service;
import common.dto.AdditionalApprovalDto;
import common.dto.event.AdditionalApprovalEventDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import edo_service.converter.AdditionalApprovalToAdditionalApprovalDtoConverter;
import edo_service.kafka.AdditionalApprovalEventProducer;
import edo_service.AdditionalApprovalValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdditionalApprovalService {


    public final AdditionalApprovalRepository additionalApprovalRepository;
    private final ApprovalRepository approvalRepository;
    private final AdditionalApprovalToAdditionalApprovalDtoConverter converter;
    private final AdditionalApprovalValidator validator;
    private final AdditionalApprovalEventProducer eventProducer;

    public AdditionalApprovalService(AdditionalApprovalRepository additionalApprovalRepository, ApprovalRepository approvalRepository, AdditionalApprovalToAdditionalApprovalDtoConverter converter, AdditionalApprovalValidator validator,AdditionalApprovalEventProducer eventProducer) {
        this.additionalApprovalRepository = additionalApprovalRepository;
        this.approvalRepository = approvalRepository;
        this.converter = converter;
        this.validator = validator;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public AdditionalApprovalDto create(AdditionalApprovalDto dto) {

        validator.validateAll(dto);


        AdditionalApproval entity = converter.toEntity(dto);


        if (dto.getApprovalId() != null) {
            Approval approval = approvalRepository.findById(dto.getApprovalId())
                    .orElseThrow(() -> new RuntimeException("Approval not found"));
            entity.setApproval(approval);
        }

        AdditionalApproval savedEntity = additionalApprovalRepository.save(entity);

        AdditionalApprovalEventDto event = new AdditionalApprovalEventDto();
        event.setAdditionalApprovalId(savedEntity.getId());
        event.setApprovalId(savedEntity.getApproval() != null ? savedEntity.getApproval().getId() : null);
        event.setType(savedEntity.getType());
        event.setStatus(savedEntity.getStatus());
        event.setEventTime(LocalDateTime.now());
        event.setEventType("CREATED");
        eventProducer.sendAdditionalApprovalEvent(event);

        return converter.toDto(savedEntity);
    }
    @Transactional
    public AdditionalApprovalDto update(AdditionalApprovalDto dto) {

        validator.validateForUpdate(dto);


        AdditionalApproval existingEntity = additionalApprovalRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Additional approval not found with id: " + dto.getId()));


        existingEntity.setType(dto.getType());
        existingEntity.setStatus(dto.getStatus());
        existingEntity.setComment(dto.getComment());
        existingEntity.setResponseDate(dto.getResponseDate());


        if (dto.getApprovalId() != null) {
            Approval approval = approvalRepository.findById(dto.getApprovalId())
                    .orElseThrow(() -> new RuntimeException("Approval not found with id: " + dto.getApprovalId()));
            existingEntity.setApproval(approval);
        }


        AdditionalApproval updatedEntity = additionalApprovalRepository.save(existingEntity);

        AdditionalApprovalEventDto event = new AdditionalApprovalEventDto();
        event.setAdditionalApprovalId(updatedEntity.getId());
        event.setApprovalId(updatedEntity.getApproval() != null ? updatedEntity.getApproval().getId() : null);
        event.setType(updatedEntity.getType());
        event.setStatus(updatedEntity.getStatus());
        event.setEventTime(LocalDateTime.now());
        event.setEventType("UPDATED");
        eventProducer.sendAdditionalApprovalEvent(event);


        return converter.toDto(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {

        if (!additionalApprovalRepository.existsById(id)) {
            throw new RuntimeException("Additional approval not found with id: " + id);
        }


        additionalApprovalRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AdditionalApprovalDto> getAll() {
        return additionalApprovalRepository.findAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

}

