package edo_service.validator;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

public class AdditionalApprovalValidatorTest {

import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)  // Включаем поддержку моков
    @DisplayName("Тесты валидатора AdditionalApprovalValidator")
    class AdditionalApprovalValidatorTest {

        @Mock
        private AdditionalApprovalRepository additionalApprovalRepository;

        @Mock
        private ApprovalRepository approvalRepository;

        @InjectMocks
        private AdditionalApprovalValidator validator;

        private Approval testApproval;
        private LocalDateTime now;
        private LocalDateTime pastDate;

        @BeforeEach
        void setUp() {
            now = LocalDateTime.now();
            pastDate = now.minusDays(5);

            testApproval = new Approval();
            testApproval.setId(1L);
            testApproval.setAppealDate(pastDate);
        }

        // ============================================================
        // 1. ТЕСТЫ ДЛЯ validateApprovalExists()
        // ============================================================

        @Test
        @DisplayName("validateApprovalExists: валидный кейс - соглашение существует")
        void testValidateApprovalExists_Valid() {
            // Подготовка: соглашение с ID=1 существует в БД
            when(approvalRepository.existsById(1L)).thenReturn(true);

            // Действие: вызываем валидацию
            assertDoesNotThrow(() -> validator.validateApprovalExists(1L));

            // Проверка: метод existsById был вызван 1 раз
            verify(approvalRepository, times(1)).existsById(1L);
        }

        @Test
        @DisplayName("validateApprovalExists: невалидный кейс - соглашение не существует")
        void testValidateApprovalExists_Invalid_NotFound() {
            // Подготовка: соглашение с ID=999 не существует
            when(approvalRepository.existsById(999L)).thenReturn(false);

            // Действие и проверка: должно выбросить исключение
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateApprovalExists(999L));

            assertTrue(exception.getMessage().contains("не найдено"));
            assertTrue(exception.getMessage().contains("999"));
        }

        @Test
        @DisplayName("validateApprovalExists: невалидный кейс - approvalId = null")
        void testValidateApprovalExists_Invalid_NullId() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateApprovalExists(null));

            assertTrue(exception.getMessage().contains("approvalId не может быть null"));

            // Проверяем, что метод репозитория НЕ вызывался
            verify(approvalRepository, never()).existsById(any());
        }

        // ============================================================
        // 2. ТЕСТЫ ДЛЯ validateNoDuplicate()
        // ============================================================

        @Test
        @DisplayName("validateNoDuplicate: валидный кейс - дубликата нет")
        void testValidateNoDuplicate_Valid_NoDuplicate() {
            // Подготовка: дубликата нет в БД
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "INTERNAL", "PENDING"))
                    .thenReturn(false);

            // Действие: проверяем
            assertDoesNotThrow(() -> validator.validateNoDuplicate(1L, "INTERNAL", "PENDING"));

            verify(additionalApprovalRepository, times(1))
                    .existsByApprovalIdAndTypeAndStatus(1L, "INTERNAL", "PENDING");
        }

        @Test
        @DisplayName("validateNoDuplicate: невалидный кейс - дубликат существует")
        void testValidateNoDuplicate_Invalid_DuplicateExists() {
            // Подготовка: дубликат СУЩЕСТВУЕТ в БД
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "INTERNAL", "APPROVED"))
                    .thenReturn(true);

            // Действие и проверка
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateNoDuplicate(1L, "INTERNAL", "APPROVED"));

            assertTrue(exception.getMessage().contains("Duplicate"));
            assertTrue(exception.getMessage().contains("approvalId=1"));
        }

        // ============================================================
        // 3. ТЕСТЫ ДЛЯ validateStatus()
        // ============================================================

        @Test
        @DisplayName("validateStatus: валидные кейсы - все разрешённые статусы")
        void testValidateStatus_Valid_AllowedStatuses() {
            // Все разрешённые статусы должны проходить
            assertDoesNotThrow(() -> validator.validateStatus("APPROVED"));
            assertDoesNotThrow(() -> validator.validateStatus("REJECTED"));
            assertDoesNotThrow(() -> validator.validateStatus("PENDING"));

            // null тоже должен проходить (пропускаем проверку)
            assertDoesNotThrow(() -> validator.validateStatus(null));
        }

        @Test
        @DisplayName("validateStatus: невалидные кейсы - запрещённые статусы")
        void testValidateStatus_Invalid_NotAllowedStatuses() {
            // Проверяем разные неверные статусы
            String[] invalidStatuses = {"APPROVE", "REJECT", "PEND", "CANCELED", "DRAFT", "", " ", "approved"}; // Регистр важен!

            for (String invalidStatus : invalidStatuses) {
                RuntimeException exception = assertThrows(RuntimeException.class,
                        () -> validator.validateStatus(invalidStatus));

                assertTrue(exception.getMessage().contains("Недопустимый статус"));
                assertTrue(exception.getMessage().contains(invalidStatus));
            }
        }

        // ============================================================
        // 4. ТЕСТЫ ДЛЯ validateCommentLength()
        // ============================================================

        @Test
        @DisplayName("validateCommentLength: валидные кейсы - комментарий в пределах 500 символов")
        void testValidateCommentLength_Valid() {
            // null - допустимо
            assertDoesNotThrow(() -> validator.validateCommentLength(null));

            // Пустая строка - допустимо
            assertDoesNotThrow(() -> validator.validateCommentLength(""));

            // 500 символов ровно
            String comment500 = "A".repeat(500);
            assertDoesNotThrow(() -> validator.validateCommentLength(comment500));

            // 250 символов
            String comment250 = "B".repeat(250);
            assertDoesNotThrow(() -> validator.validateCommentLength(comment250));
        }

        @Test
        @DisplayName("validateCommentLength: невалидный кейс - комментарий длиннее 500 символов")
        void testValidateCommentLength_Invalid_TooLong() {
            // 501 символ
            String comment501 = "C".repeat(501);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateCommentLength(comment501));

            assertTrue(exception.getMessage().contains("превышает 500 символов"));
            assertTrue(exception.getMessage().contains("501"));
        }

        // ============================================================
        // 5. ТЕСТЫ ДЛЯ validateResponseDate()
        // ============================================================

        @Test
        @DisplayName("validateResponseDate: валидные кейсы - корректные даты")
        void testValidateResponseDate_Valid() {
            // null - допустимо
            assertDoesNotThrow(() -> validator.validateResponseDate(null, 1L));

            // Дата между appealDate и текущим моментом
            when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.of(testApproval));
            LocalDateTime validDate = now.minusDays(2);  // между pastDate и now
            assertDoesNotThrow(() -> validator.validateResponseDate(validDate, 1L));

            // Дата равна appealDate - допустимо
            LocalDateTime equalToAppeal = pastDate;
            assertDoesNotThrow(() -> validator.validateResponseDate(equalToAppeal, 1L));

            // Дата равна текущему моменту - допустимо
            assertDoesNotThrow(() -> validator.validateResponseDate(now, 1L));
        }

        @Test
        @DisplayName("validateResponseDate: невалидный кейс - дата в будущем")
        void testValidateResponseDate_Invalid_FutureDate() {
            when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.of(testApproval));
            LocalDateTime futureDate = now.plusDays(1);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateResponseDate(futureDate, 1L));

            assertTrue(exception.getMessage().contains("не может быть в будущем"));
        }

        @Test
        @DisplayName("validateResponseDate: невалидный кейс - дата раньше appealDate")
        void testValidateResponseDate_Invalid_BeforeAppealDate() {
            when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.of(testApproval));
            LocalDateTime beforeAppeal = pastDate.minusDays(1);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateResponseDate(beforeAppeal, 1L));

            assertTrue(exception.getMessage().contains("не может быть раньше"));
            assertTrue(exception.getMessage().contains("appealDate"));
        }

        // ============================================================
        // 6. ТЕСТ ДЛЯ КОМБИНИРОВАННОГО МЕТОДА validateAll()
        // ============================================================

        @Test
        @DisplayName("validateAll: валидный кейс - все проверки проходят")
        void testValidateAll_Valid() {
            // Создаём DTO с корректными данными
            AdditionalApprovalDto dto = new AdditionalApprovalDto();
            dto.setApprovalId(1L);
            dto.setType("INTERNAL");
            dto.setStatus("PENDING");
            dto.setComment("Всё хорошо");
            dto.setResponseDate(now.minusDays(1));

            // Настраиваем моки
            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "INTERNAL", "PENDING"))
                    .thenReturn(false);
            when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.of(testApproval));

            // Действие: все проверки должны пройти
            assertDoesNotThrow(() -> validator.validateAll(dto));
        }

        @Test
        @DisplayName("validateAll: невалидный кейс - первая же ошибка прерывает выполнение")
        void testValidateAll_Invalid_FirstErrorStops() {
            // Создаём DTO с несколькими ошибками
            AdditionalApprovalDto dto = new AdditionalApprovalDto();
            dto.setApprovalId(999L);  // Несуществующий ID
            dto.setStatus("INVALID");  // Неверный статус
            dto.setComment("A".repeat(600));  // Слишком длинный комментарий

            // Первая проверка (validateApprovalExists) должна выбросить ошибку
            when(approvalRepository.existsById(999L)).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(dto));

            // Должна быть ошибка о несуществующем соглашении, а не о статусе или комментарии
            assertTrue(exception.getMessage().contains("не найдено"));
        }
    }
}
