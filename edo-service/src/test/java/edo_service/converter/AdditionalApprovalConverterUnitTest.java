package edo_service.converter;

import edo_repository.entity.AdditionalApproval;
import common.dto.AdditionalApprovalDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdditionalApprovalConverterUnitTest {

    // Создаем экземпляр конвертера
    private final AdditionalApprovalToAdditionalApprovalDtoConverter converter =
            new AdditionalApprovalToAdditionalApprovalDtoConverter();

    @Test
    void testEntityToDtoConversion() {
        // 1. ПОДГОТОВКА: Создаем тестовую сущность
        AdditionalApproval entity = new AdditionalApproval();
        entity.setId(100L);
        entity.setType("TEST");
        entity.setStatus("PENDING");
        entity.setComment("Test comment");

        // 2. ВЫПОЛНЕНИЕ: Конвертируем Entity в DTO
        AdditionalApprovalDto dto = converter.toDto(entity);

        // 3. ПРОВЕРКА: Проверяем что данные правильно сконвертировались
        assertNotNull(dto, "DTO не должен быть null");
        assertEquals(100L, dto.getId(), "ID должен совпадать");
        assertEquals("TEST", dto.getType(), "Type должен совпадать");
        assertEquals("PENDING", dto.getStatus(), "Status должен совпадать");
        assertEquals("Test comment", dto.getComment(), "Comment должен совпадать");

        System.out.println("✅ Entity → DTO конвертация работает!");
    }

    @Test
    void testDtoToEntityConversion() {
        // 1. ПОДГОТОВКА: Создаем тестовый DTO
        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setId(200L);
        dto.setApprovalId(1L);
        dto.setType("EXTRA");
        dto.setStatus("APPROVED");
        dto.setComment("Another test");

        // 2. ВЫПОЛНЕНИЕ: Конвертируем DTO в Entity
        AdditionalApproval entity = converter.toEntity(dto);

        // 3. ПРОВЕРКА: Проверяем что данные правильно сконвертировались
        assertNotNull(entity, "Entity не должен быть null");
        assertEquals(200L, entity.getId(), "ID должен совпадать");
        assertEquals("EXTRA", entity.getType(), "Type должен совпадать");
        assertEquals("APPROVED", entity.getStatus(), "Status должен совпадать");
        assertEquals("Another test", entity.getComment(), "Comment должен совпадать");

        System.out.println("✅ DTO → Entity конвертация работает!");
    }

    @Test
    void testFullCycleConversion() {
        // 1. ПОДГОТОВКА: Создаем исходный DTO
        AdditionalApprovalDto originalDto = new AdditionalApprovalDto();
        originalDto.setApprovalId(999L);
        originalDto.setType("FULL_CYCLE");
        originalDto.setStatus("COMPLETED");
        originalDto.setComment("Full cycle test");

        // 2. ВЫПОЛНЕНИЕ: DTO → Entity → DTO
        AdditionalApproval entity = converter.toEntity(originalDto);
        AdditionalApprovalDto resultDto = converter.toDto(entity);

        // 3. ПРОВЕРКА: Проверяем что данные сохранились после полного цикла
        assertNotNull(resultDto, "Результат не должен быть null");
        assertEquals(originalDto.getType(), resultDto.getType(), "Type должен сохраниться");
        assertEquals(originalDto.getStatus(), resultDto.getStatus(), "Status должен сохраниться");
        assertEquals(originalDto.getComment(), resultDto.getComment(), "Comment должен сохраниться");

        System.out.println("✅ ПОЛНЫЙ ЦИКЛ КОНВЕРТАЦИИ РАБОТАЕТ!");
        System.out.println("Исходные данные: " + originalDto.getType() + ", " + originalDto.getStatus());
        System.out.println("Результат: " + resultDto.getType() + ", " + resultDto.getStatus());
    }

    @Test
    void testNullSafety() {
        // Проверяем что конвертер корректно обрабатывает null
        assertNull(converter.toDto(null), "toDto(null) должен возвращать null");
        assertNull(converter.toEntity(null), "toEntity(null) должен возвращать null");

        System.out.println("✅ ОБРАБОТКА NULL РАБОТАЕТ!");
    }
}