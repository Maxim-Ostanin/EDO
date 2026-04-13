// src/main/java/com/example/edo/service/AdditionalApprovalService.java
package com.example.edo.service;

import com.example.edo.dto.AdditionalApprovalDto;
import com.example.edo.entity.AdditionalApproval;
import com.example.edo.mapper.AdditionalApprovalMapper;
import com.example.edo.repository.AdditionalApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdditionalApprovalService {

    private final AdditionalApprovalRepository repository;
    private final AdditionalApprovalMapper mapper;

    public <AdditionalApprovalDto> AdditionalApprovalDto findById(Long id) {
        AdditionalApproval entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return mapper.toDto(entity);
    }
}

