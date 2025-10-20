package edo_service.controller;

import edo_service.converter.AdditionalApprovalToAdditionalApprovalDtoConverter;
import edo_repository.entity.AdditionalApproval;
import common.dto.AdditionalApprovalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/converter")
public class TestConverterController {

    @Autowired
    private AdditionalApprovalToAdditionalApprovalDtoConverter converter;

    @PostMapping("/entity-to-dto")
    public AdditionalApprovalDto testEntityToDto(@RequestBody AdditionalApproval entity) {
        System.out.println("=== ТЕСТ Entity → DTO ===");
        System.out.println("Входная Entity: " + entity);

        AdditionalApprovalDto dto = converter.toDto(entity);

        System.out.println("Выходная DTO: " + dto);
        System.out.println("=== ТЕСТ ЗАВЕРШЕН ===");

        return dto;
    }

    @PostMapping("/dto-to-entity")
    public AdditionalApproval testDtoToEntity(@RequestBody AdditionalApprovalDto dto) {
        System.out.println("=== ТЕСТ DTO → Entity ===");
        System.out.println("Входная DTO: " + dto);

        AdditionalApproval entity = converter.toEntity(dto);

        System.out.println("Выходная Entity: " + entity);
        System.out.println("=== ТЕСТ ЗАВЕРШЕН ===");

        return entity;
    }

    @PostMapping("/full-cycle")
    public AdditionalApprovalDto testFullCycle(@RequestBody AdditionalApprovalDto originalDto) {
        System.out.println("=== ТЕСТ ПОЛНЫЙ ЦИКЛ ===");
        System.out.println("Исходная DTO: " + originalDto);

        // DTO → Entity → DTO
        AdditionalApproval entity = converter.toEntity(originalDto);
        System.out.println("Промежуточная Entity: " + entity);

        AdditionalApprovalDto resultDto = converter.toDto(entity);
        System.out.println("Результатная DTO: " + resultDto);

        System.out.println("=== ТЕСТ ЗАВЕРШЕН ===");

        return resultDto;
    }
}