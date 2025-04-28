package common.dto;

import edo_repository.entity.enums.ApprovalStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApprovalDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidApprovalDto() {
        ApprovalDto approvalDto = new ApprovalDto(1L, 2L, ApprovalStatus.APPROVED, "Комментарий", LocalDateTime.now());

        Set<ConstraintViolation<ApprovalDto>> violations = validator.validate(approvalDto);
        assertTrue(violations.isEmpty(), "✅ Валидный `ApprovalDto` не должен иметь ошибок!");
    }

    @Test
    public void testInvalidNullFields() {
        ApprovalDto approvalDto = new ApprovalDto(null, null, null, "", null);

        Set<ConstraintViolation<ApprovalDto>> violations = validator.validate(approvalDto);
        assertFalse(violations.isEmpty(), "❌ `ApprovalDto` с `null` значениями должен иметь ошибки!");
    }

    @Test
    public void testInvalidFutureResponseDate() {
        ApprovalDto approvalDto = new ApprovalDto(1L, 2L, ApprovalStatus.APPROVED, "Комментарий", LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<ApprovalDto>> violations = validator.validate(approvalDto);
        assertFalse(violations.isEmpty(), "❌ Дата ответа из будущего должна вызывать ошибку!");
    }

    @Test
    public void testInvalidCommentSize() {
        String longComment = "A".repeat(501); // 501 символ, лимит — 500
        ApprovalDto approvalDto = new ApprovalDto(1L, 2L, ApprovalStatus.APPROVED, longComment, LocalDateTime.now());

        Set<ConstraintViolation<ApprovalDto>> violations = validator.validate(approvalDto);
        assertFalse(violations.isEmpty(), "❌ Комментарий длиннее 500 символов должен вызывать ошибку!");
    }
}
