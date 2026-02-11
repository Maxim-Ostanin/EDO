package edo_public_api.controller;



import common.dto.AdditionalApprovalDto;
import edo_service.service.AdditionalApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/additional-approval")
@RequiredArgsConstructor
public class AdditionalApprovalController {

    private final AdditionalApprovalService additionalApprovalService;

    //Создал в контроллере метод на сохранение
    @PostMapping
    public AdditionalApprovalDto saveAddApproval (@Valid @RequestBody AdditionalApprovalDto dto) {

        return additionalApprovalService.saveAddApproval(dto);
    }

    //Создал в контроллере метод на показ (вывод)
    @GetMapping("/{id}")
    public AdditionalApprovalDto viewAddApproval (@Valid @PathVariable Long id) {

        return additionalApprovalService.findById(id);
    }


    //Создал в контроллере метод на удаление
    @DeleteMapping("/{id}")
    public void deleteAddApproval (@Valid @PathVariable Long id) {
        additionalApprovalService.deleteById(id);
    }

    //Создал в контроллере метод на редактирование
    @PutMapping("/{id}")
    public AdditionalApprovalDto editAddApproval (@Valid @PathVariable Long id, @RequestBody AdditionalApprovalDto dto) {

        return additionalApprovalService.editAddApproval(id, dto);
    }

}
