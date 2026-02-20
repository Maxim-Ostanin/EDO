package edo_public_api.controller;



import common.dto.AdditionalApprovalDto;
import edo_public_api.kafka.EdoProducer;
import edo_service.service.AdditionalApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/additional-approval")
@RequiredArgsConstructor
public class AdditionalApprovalController {

    private final EdoProducer edoProducer;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public void saveAddApproval (@Valid @RequestBody AdditionalApprovalDto dto) {
        edoProducer.addAppSave(dto);
    }

    @GetMapping("/{id}")
    public AdditionalApprovalDto viewAddApproval (@Valid @PathVariable Long id) throws Exception {
        return edoProducer.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAddApproval (@Valid @PathVariable Long id) {
        edoProducer.addAppDelete(id);
    }

    @PutMapping("/{id}")
    public AdditionalApprovalDto editAddApproval (@Valid @PathVariable Long id, @RequestBody AdditionalApprovalDto dto) throws Exception {
        return edoProducer.editAddApproval(id, dto);
    }

}
