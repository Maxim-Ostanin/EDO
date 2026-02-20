package edo_service.serviceTest;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ValidatorTest {

    @InjectMocks
    private Validator validator;
    @Mock
    private ApprovalRepository approvalRepository;
    @Mock
    private AdditionalApprovalRepository additionalApprovalRepository;

    @Test
    @DisplayName("Проверка: ошибка при некорректном статусе")
    public void validateWrongStatus() {
        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setApprovalId(1L);
        dto.setStatus("BANNED");

        when(approvalRepository.findById(1L)).thenReturn(Optional.of(new Approval()));

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class, () -> validator.validate(dto));

        Assertions.assertEquals("Статус может быть только: APPROVED, REJECTED, PENDING", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка: ошибка при слишком длинном комментарии")
    public void validateFieldsLength () {

        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setApprovalId(1L);
        dto.setStatus("APPROVED");
        dto.setComment("a".repeat(501));

        when(approvalRepository.findById(1L)).thenReturn(Optional.of(new Approval()));

        RuntimeException exception =
                Assertions.assertThrows(
                        RuntimeException.class, () ->
                        {validator.validate(dto);});

        Assertions.assertEquals("Комментарий превысил 500 символов!", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка 3.1: ошибка, если основное соглашение не найдено")
    public void validateRelationsNotFound() {

        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setApprovalId(1L);
        dto.setStatus("APPROVED");
        dto.setResponseDate(LocalDateTime.now().minusDays(5));

        when(approvalRepository.findById(dto.getApprovalId())).thenReturn(Optional.empty());
        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class, () -> validator.validate(dto));

        String expectedMessage = "Ошибка: Соглашение с ID " + dto.getApprovalId() + " не найдено!";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    @DisplayName("Проверка 3.2.1: Ошибка, если дата допа раньше основного соглашения")
    public void validateDateBeforeApproval() {

        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setApprovalId(1L);
        dto.setStatus("APPROVED");
        dto.setResponseDate(LocalDateTime.now().minusDays(2)); // Завтра

        Approval approval = new Approval();
        approval.setResponseDate(LocalDateTime.now().minusDays(1));

        when(approvalRepository.findById(1L)).thenReturn(Optional.of(approval));

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class, () -> validator.validate(dto));

        Assertions.assertTrue(exception.getMessage().contains("не может быть раньше даты заключения"));
    }

    @Test
    @DisplayName("Проверка 3.2.2: Ошибка, если дата в будущем")
    public void validateDateInFuture() {

        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setApprovalId(1L);
        dto.setStatus("APPROVED");
        dto.setResponseDate(LocalDateTime.now().plusDays(1)); // Завтра

        Approval approval = new Approval();
        approval.setResponseDate(LocalDateTime.now().minusDays(1));
        when(approvalRepository.findById(1L)).thenReturn(Optional.of(approval));

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class, () -> validator.validate(dto));

        Assertions.assertTrue(exception.getMessage().contains("не может быть в будущем"));
    }

    //Тест для 4-ой валидации: проверка дубликата
    @Test
    @DisplayName("Проверка: добавления дубликата")
    public void validateDuplicate () {

        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setApprovalId(10L);
        dto.setType("LEGAL");
        dto.setStatus("APPROVED");
        dto.setResponseDate(LocalDateTime.now());

        Approval approval = new Approval();
        approval.setId(10L);
        approval.setResponseDate(LocalDateTime.now().minusDays(1));
        when(approvalRepository.findById(10L)).thenReturn(Optional.of(approval));
        when(additionalApprovalRepository.existsByApprovalIdAndType(10L, "LEGAL"))
                .thenReturn(true);

        RuntimeException exception =
                Assertions.assertThrows(
                        RuntimeException.class, () ->
                        {validator.validate(dto);});

        Assertions.assertEquals("Такое соглашение с типом: LEGAL уже зарегистрировано", exception.getMessage());
    }

    // 5. Тест для успешного выполнения кода
    @Test
    @DisplayName("Проверка: успешная валидация (все данные верны)")
    public void validateSuccess() {

        AdditionalApprovalDto dto = new AdditionalApprovalDto();
        dto.setApprovalId(1L);
        dto.setStatus("APPROVED");
        dto.setType("LEGAL");
        dto.setComment("Всё хорошо");
        dto.setResponseDate(LocalDateTime.now());

        Approval approval = new Approval();
        approval.setResponseDate(LocalDateTime.now().minusDays(1));

        when(approvalRepository.findById(1L)).thenReturn(Optional.of(approval));
        when(additionalApprovalRepository.existsByApprovalIdAndType(1L, "LEGAL")).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> validator.validate(dto));
    }
}


