package edo_service.service;

import common.dto.AdditionalApprovalDto;
import java.util.List;
import java.util.Optional;

public interface AdditionalApprovalService {

    AdditionalApprovalDto save(AdditionalApprovalDto dto);

    AdditionalApprovalDto update(Long id, AdditionalApprovalDto dto);

    Optional<AdditionalApprovalDto> findById(Long id);

    Optional<AdditionalApprovalDto> findByApprovalId(Long approvalId);

    List<AdditionalApprovalDto> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}