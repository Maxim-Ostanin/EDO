package edo_service.validation;

import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import common.dto.AdditionalApprovalDto;
import edo_service.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdditionalApprovalValidatorTest {

    @Mock
    private AdditionalApprovalRepository additionalApprovalRepository;

    @Mock
    private ApprovalRepository approvalRepository;

    @InjectMocks
    private AdditionalApprovalValidator validator;

    @BeforeEach
    void setUp() {
        System.out.println("=== SETUP ВЫПОЛНЕН ===");
    }

    @Test
    void validateForCreate_Success() {
        System.out.println("=== ТЕСТ validateForCreate_Success ЗАПУЩЕН ===");

        // Given
        AdditionalApprovalDto dto = createValidDto();

        when(approvalRepository.existsById(anyLong())).thenReturn(true);
        when(additionalApprovalRepository.existsByApprovalIdAndType(anyLong(), anyString())).thenReturn(false);
        when(approvalRepository.findAppealDateByApprovalId(anyLong())).thenReturn(LocalDateTime.now().minusDays(1));

        // When & Then
        assertDoesNotThrow(() -> validator.validateForCreate(dto));

        // Verify interactions
        verify(approvalRepository).existsById(1L);
        verify(additionalApprovalRepository).existsByApprovalIdAndType(1L, "TYPE_A");

        System.out.println("=== ТЕСТ validateForCreate_Success ПРОЙДЕН ===");
    }

    @Test
    void validateForCreate_DuplicateThrowsException() {
        System.out.println("=== ТЕСТ validateForCreate_DuplicateThrowsException ЗАПУЩЕН ===");

        // Given
        AdditionalApprovalDto dto = createValidDto();

        when(approvalRepository.existsById(anyLong())).thenReturn(true);
        when(additionalApprovalRepository.existsByApprovalIdAndType(anyLong(), anyString())).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForCreate(dto));

        assertTrue(exception.getMessage().contains("Дублирующее дополнительное согласование"));
        System.out.println("=== ТЕСТ validateForCreate_DuplicateThrowsException ПРОЙДЕН ===");
    }

    @Test
    void validateForCreate_ApprovalNotFoundThrowsException() {
        System.out.println("=== ТЕСТ validateForCreate_ApprovalNotFoundThrowsException ЗАПУЩЕН ===");

        // Given
        AdditionalApprovalDto dto = createValidDto();

        when(approvalRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForCreate(dto));

        assertTrue(exception.getMessage().contains("Основное соглашение с ID"));
        System.out.println("=== ТЕСТ validateForCreate_ApprovalNotFoundThrowsException ПРОЙДЕН ===");
    }

    @Test
    void validateForCreate_InvalidStatusThrowsException() {
        System.out.println("=== ТЕСТ validateForCreate_InvalidStatusThrowsException ЗАПУЩЕН ===");

        // Given
        AdditionalApprovalDto dto = createValidDto();
        dto.setStatus("INVALID_STATUS");

        when(approvalRepository.existsById(anyLong())).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForCreate(dto));

        assertTrue(exception.getMessage().contains("Недопустимый статус"));
        System.out.println("=== ТЕСТ validateForCreate_InvalidStatusThrowsException ПРОЙДЕН ===");
    }

    @Test
    void validateForCreate_CommentTooLongThrowsException() {
        System.out.println("=== ТЕСТ validateForCreate_CommentTooLongThrowsException ЗАПУЩЕН ===");

        // Given
        AdditionalApprovalDto dto = createValidDto();
        dto.setComment("a".repeat(501)); // 501 символов

        when(approvalRepository.existsById(anyLong())).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForCreate(dto));

        assertTrue(exception.getMessage().contains("Комментарий не может превышать 500 символов"));
        System.out.println("=== ТЕСТ validateForCreate_CommentTooLongThrowsException ПРОЙДЕН ===");
    }

    @Test
    void validateForCreate_ResponseDateInFutureThrowsException() {
        System.out.println("=== ТЕСТ validateForCreate_ResponseDateInFutureThrowsException ЗАПУЩЕН ===");

        // Given
        AdditionalApprovalDto dto = createValidDto();
        dto.setResponseDate(LocalDateTime.now().plusDays(1)); // Дата в будущем

        when(approvalRepository.existsById(anyLong())).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForCreate(dto));

        assertTrue(exception.getMessage().contains("Дата ответа не может быть в будущем"));
        System.out.println("=== ТЕСТ validateForCreate_ResponseDateInFutureThrowsException ПРОЙДЕН ===");
    }

    @Test
    void validateForCreate_AllValidationsPass() {
        System.out.println("=== ТЕСТ validateForCreate_AllValidationsPass ЗАПУЩЕН ===");

        // Given - все данные валидны
        AdditionalApprovalDto dto = createValidDto();

        when(approvalRepository.existsById(anyLong())).thenReturn(true);
        when(additionalApprovalRepository.existsByApprovalIdAndType(anyLong(), anyString())).thenReturn(false);
        when(approvalRepository.findAppealDateByApprovalId(anyLong())).thenReturn(LocalDateTime.now().minusDays(1));

        // When & Then - не должно быть исключений
        assertDoesNotThrow(() -> validator.validateForCreate(dto));

        System.out.println("=== ТЕСТ validateForCreate_AllValidationsPass ПРОЙДЕН ===");
    }

    @Test
    void validateStatus_AllAllowedStatuses() {
        System.out.println("=== ТЕСТ validateStatus_AllAllowedStatuses ЗАПУЩЕН ===");

        // Given - все разрешенные статусы
        String[] allowedStatuses = {"APPROVED", "REJECTED", "PENDING"};

        // Настраиваем моки один раз для всех статусов
        when(approvalRepository.existsById(anyLong())).thenReturn(true);
        when(additionalApprovalRepository.existsByApprovalIdAndType(anyLong(), anyString())).thenReturn(false);
        when(approvalRepository.findAppealDateByApprovalId(anyLong())).thenReturn(LocalDateTime.now().minusDays(1));

        for (String status : allowedStatuses) {
            System.out.println("Проверка статуса: " + status);

            // Создаем DTO с текущим статусом
            AdditionalApprovalDto dto = createValidDto();
            dto.setStatus(status);

            // When & Then - не должно быть исключений
            assertDoesNotThrow(() -> validator.validateForCreate(dto));
        }

        System.out.println("=== ТЕСТ validateStatus_AllAllowedStatuses ПРОЙДЕН ===");
    }

    @Test
    void validateComment_ExactMaxLength() {
        System.out.println("=== ТЕСТ validateComment_ExactMaxLength ЗАПУЩЕН ===");

        // Given - комментарий точно 500 символов
        AdditionalApprovalDto dto = createValidDto();
        dto.setComment("a".repeat(500));

        when(approvalRepository.existsById(anyLong())).thenReturn(true);
        when(additionalApprovalRepository.existsByApprovalIdAndType(anyLong(), anyString())).thenReturn(false);
        when(approvalRepository.findAppealDateByApprovalId(anyLong())).thenReturn(LocalDateTime.now().minusDays(1));

        // When & Then - не должно быть исключений
        assertDoesNotThrow(() -> validator.validateForCreate(dto));

        System.out.println("=== ТЕСТ validateComment_ExactMaxLength ПРОЙДЕН ===");
    }

    @Test
    void validateComment_NullComment() {
        System.out.println("=== ТЕСТ validateComment_NullComment ЗАПУЩЕН ===");

        // Given - комментарий null
        AdditionalApprovalDto dto = createValidDto();
        dto.setComment(null);

        when(approvalRepository.existsById(anyLong())).thenReturn(true);
        when(additionalApprovalRepository.existsByApprovalIdAndType(anyLong(), anyString())).thenReturn(false);
        when(approvalRepository.findAppealDateByApprovalId(anyLong())).thenReturn(LocalDateTime.now().minusDays(1));

        // When & Then - не должно быть исключений
        assertDoesNotThrow(() -> validator.validateForCreate(dto));

        System.out.println("=== ТЕСТ validateComment_NullComment ПРОЙДЕН ===");
    }

    @Test
    void validateResponseDate_NullResponseDate() {
        System.out.println("=== ТЕСТ validateResponseDate_NullResponseDate ЗАПУЩЕН ===");

        // Given - дата ответа null
        AdditionalApprovalDto dto = createValidDto();
        dto.setResponseDate(null);

        when(approvalRepository.existsById(anyLong())).thenReturn(true);
        when(additionalApprovalRepository.existsByApprovalIdAndType(anyLong(), anyString())).thenReturn(false);

        // When & Then - не должно быть исключений
        assertDoesNotThrow(() -> validator.validateForCreate(dto));

        System.out.println("=== ТЕСТ validateResponseDate_NullResponseDate ПРОЙДЕН ===");
    }

    @Test
    void validateResponseDate_ExactAppealDate() {
        System.out.println("=== ТЕСТ validateResponseDate_ExactAppealDate ЗАПУЩЕН ===");

        // Given - дата ответа равна дате обращения
        LocalDateTime appealDate = LocalDateTime.now().minusDays(5);
        AdditionalApprovalDto dto = createValidDto();
        dto.setResponseDate(appealDate);

        when(approvalRepository.existsById(anyLong())).thenReturn(true);
        when(additionalApprovalRepository.existsByApprovalIdAndType(anyLong(), anyString())).thenReturn(false);
        when(approvalRepository.findAppealDateByApprovalId(anyLong())).thenReturn(appealDate);

        // When & Then - не должно быть исключений
        assertDoesNotThrow(() -> validator.validateForCreate(dto));

        System.out.println("=== ТЕСТ validateResponseDate_ExactAppealDate ПРОЙДЕН ===");
    }

    @Test
    void validateResponseDate_BeforeAppealDateThrowsException() {
        System.out.println("=== ТЕСТ validateResponseDate_BeforeAppealDateThrowsException ЗАПУЩЕН ===");

        // Given - дата ответа раньше даты обращения
        LocalDateTime appealDate = LocalDateTime.now().minusDays(5);
        AdditionalApprovalDto dto = createValidDto();
        dto.setResponseDate(appealDate.minusDays(1)); // На день раньше обращения

        when(approvalRepository.existsById(anyLong())).thenReturn(true);
        when(additionalApprovalRepository.existsByApprovalIdAndType(anyLong(), anyString())).thenReturn(false);
        when(approvalRepository.findAppealDateByApprovalId(anyLong())).thenReturn(appealDate);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForCreate(dto));

        assertTrue(exception.getMessage().contains("не может быть раньше даты обращения"));
        System.out.println("=== ТЕСТ validateResponseDate_BeforeAppealDateThrowsException ПРОЙДЕН ===");
    }

    @Test
    void simpleConstructorTest() {
        System.out.println("=== ПРОСТОЙ ТЕСТ КОНСТРУКТОРА ===");
        assertNotNull(validator);
        assertNotNull(additionalApprovalRepository);
        assertNotNull(approvalRepository);
        System.out.println("Все зависимости инициализированы корректно");
    }

    private AdditionalApprovalDto createValidDto() {
        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setApprovalId(1L);
        dto.setType("TYPE_A");
        dto.setStatus("APPROVED");
        dto.setComment("Valid comment");
        dto.setResponseDate(LocalDateTime.now().minusHours(1));
        return dto;
    }
}