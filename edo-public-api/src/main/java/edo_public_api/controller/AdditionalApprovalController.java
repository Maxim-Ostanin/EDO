package edo_public_api.controller;

import common.dto.AdditionalApprovalDto;
import edo_service.service.AdditionalApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/additional-approvals")
@RequiredArgsConstructor
public class AdditionalApprovalController {

    private final AdditionalApprovalService additionalApprovalService;

    @PostMapping
    public ResponseEntity<AdditionalApprovalDto> create(@Valid @RequestBody AdditionalApprovalDto dto) {
        AdditionalApprovalDto created = additionalApprovalService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdditionalApprovalDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AdditionalApprovalDto dto) {
        AdditionalApprovalDto updated = additionalApprovalService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdditionalApprovalDto> getById(@PathVariable Long id) {
        return additionalApprovalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-approval/{approvalId}")
    public ResponseEntity<AdditionalApprovalDto> getByApprovalId(@PathVariable Long approvalId) {
        return additionalApprovalService.findByApprovalId(approvalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AdditionalApprovalDto>> getAll() {
        List<AdditionalApprovalDto> all = additionalApprovalService.findAll();
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        additionalApprovalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}