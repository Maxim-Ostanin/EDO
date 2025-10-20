package edo_service.service.impl;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_service.converter.AdditionalApprovalToAdditionalApprovalDtoConverter;
import edo_service.exception.AdditionalApprovalNotFoundException;
import edo_service.service.AdditionalApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdditionalApprovalServiceImpl implements AdditionalApprovalService {

    private final AdditionalApprovalRepository additionalApprovalRepository;
    private final AdditionalApprovalToAdditionalApprovalDtoConverter converter;

    @Override
    public AdditionalApprovalDto save(AdditionalApprovalDto dto) {
        AdditionalApproval entity = converter.toEntity(dto);

        if (dto.getApprovalId() != null) {
            Approval approval = new Approval();
            approval.setId(dto.getApprovalId());
            entity.setApproval(approval);
        }

        AdditionalApproval saved = additionalApprovalRepository.save(entity);
        return converter.toDto(saved);
    }

    @Override
    public AdditionalApprovalDto update(Long id, AdditionalApprovalDto dto) {
        AdditionalApproval existing = additionalApprovalRepository.findById(id)
                .orElseThrow(() -> new AdditionalApprovalNotFoundException(
                        "Дополнительное согласование не найдено с id: " + id));

        converter.updateEntityFromDto(dto, existing);

        if (dto.getApprovalId() != null && !dto.getApprovalId().equals(existing.getApproval().getId())) {
            Approval approval = new Approval();
            approval.setId(dto.getApprovalId());
            existing.setApproval(approval);
        }

        AdditionalApproval updated = additionalApprovalRepository.save(existing);
        return converter.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdditionalApprovalDto> findById(Long id) {
        return additionalApprovalRepository.findById(id)
                .map(converter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdditionalApprovalDto> findByApprovalId(Long approvalId) {
        return additionalApprovalRepository.findByApprovalId(approvalId)
                .map(converter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdditionalApprovalDto> findAll() {
        return additionalApprovalRepository.findAll()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        if (!additionalApprovalRepository.existsById(id)) {
            throw new AdditionalApprovalNotFoundException(
                    "Дополнительное согласование не найдено с id: " + id);
        }
        additionalApprovalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return additionalApprovalRepository.existsById(id);
    }
}