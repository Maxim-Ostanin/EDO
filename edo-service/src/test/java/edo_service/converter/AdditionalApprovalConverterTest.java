package edo_service.converter;

import edo_repository.entity.AdditionalApproval;
import common.dto.AdditionalApprovalDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdditionalApprovalConverterTest {

    @Autowired
    private AdditionalApprovalToAdditionalApprovalDtoConverter converter;

    @Test
    void testConverterToDto() {
        // Given - создаем сущность
        AdditionalApproval entity = new AdditionalApproval();
        entity.setId(100L);
        entity.setType("TEST");
        entity.setStatus("PENDING");
        entity.setComment("Test comment");

        // When - конвертируем в DTO
        AdditionalApprovalDto dto = converter.toDto(entity);

        // Then - проверяем результат
        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("TEST", dto.getType());
        assertEquals("PENDING", dto.getStatus());
        assertEquals("Test comment", dto.getComment());

        System.out.println("✅ Entity → DTO конвертация работает!");
    }
}