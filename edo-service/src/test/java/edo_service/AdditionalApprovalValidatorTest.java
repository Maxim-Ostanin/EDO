package edo_service;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import edo_service.AdditionalApprovalValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdditionalApprovalValidator Tests")
class AdditionalApprovalValidatorTest {

    @Mock
    private AdditionalApprovalRepository additionalApprovalRepository;

    @Mock
    private ApprovalRepository approvalRepository;

    @InjectMocks
    private AdditionalApprovalValidator validator;

    private AdditionalApprovalDto validDto;
    private Approval validApproval;

    @BeforeEach
    void setUp() {
        validDto = new AdditionalApprovalDto();
        validDto.setApprovalId(1L);
        validDto.setType("SIGN");
        validDto.setStatus("PENDING");
        validDto.setComment("Valid comment");
        validDto.setResponseDate(LocalDateTime.now().minusDays(1));

        validApproval = new Approval();
        validApproval.setId(1L);
        validApproval.setAppealDate(LocalDateTime.now().minusDays(2));
    }

    // ==================== ТЕСТЫ ДЛЯ validateNoDuplicate ====================

    @Nested
    @DisplayName("validateNoDuplicate Tests")
    class ValidateNoDuplicateTests {

        @Test
        @DisplayName("Should not throw exception when duplicate does not exist")
        void testValidateNoDuplicate_NoDuplicate() {
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "SIGN", "PENDING"))
                    .thenReturn(false);

            assertDoesNotThrow(() -> validator.validateNoDuplicate(1L, "SIGN", "PENDING"));
        }

        @Test
        @DisplayName("Should throw exception when duplicate exists")
        void testValidateNoDuplicate_DuplicateExists() {
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "SIGN", "PENDING"))
                    .thenReturn(true);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateNoDuplicate(1L, "SIGN", "PENDING"));

            assertTrue(exception.getMessage().contains("Duplicate"));
            assertTrue(exception.getMessage().contains("approvalId=1"));
        }
    }

    // ==================== ТЕСТЫ ДЛЯ validateApprovalExists ====================

    @Nested
    @DisplayName("validateApprovalExists Tests")
    class ValidateApprovalExistsTests {

        @Test
        @DisplayName("Should throw exception when approvalId is null")
        void testValidateApprovalExists_NullId() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateApprovalExists(null));

            assertEquals("approvalId не может быть null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when approval does not exist")
        void testValidateApprovalExists_NotFound() {
            when(approvalRepository.existsById(1L)).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateApprovalExists(1L));

            assertTrue(exception.getMessage().contains("Связанное соглашение не найдено"));
        }

        @Test
        @DisplayName("Should not throw exception when approval exists")
        void testValidateApprovalExists_Found() {
            when(approvalRepository.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> validator.validateApprovalExists(1L));
        }
    }

    // ==================== ТЕСТЫ ДЛЯ validateStatus ====================

    @Nested
    @DisplayName("validateStatus Tests")
    class ValidateStatusTests {

        @Test
        @DisplayName("Should not throw exception when status is null")
        void testValidateStatus_NullStatus() {
            assertDoesNotThrow(() -> validator.validateStatus(null));
        }

        @Test
        @DisplayName("Should not throw exception when status is APPROVED")
        void testValidateStatus_Approved() {
            assertDoesNotThrow(() -> validator.validateStatus("APPROVED"));
        }

        @Test
        @DisplayName("Should not throw exception when status is REJECTED")
        void testValidateStatus_Rejected() {
            assertDoesNotThrow(() -> validator.validateStatus("REJECTED"));
        }

        @Test
        @DisplayName("Should not throw exception when status is PENDING")
        void testValidateStatus_Pending() {
            assertDoesNotThrow(() -> validator.validateStatus("PENDING"));
        }

        @Test
        @DisplayName("Should throw exception when status is invalid")
        void testValidateStatus_InvalidStatus() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateStatus("INVALID_STATUS"));

            assertTrue(exception.getMessage().contains("Недопустимый статус"));
            assertTrue(exception.getMessage().contains("APPROVED, REJECTED, PENDING"));
        }

        @Test
        @DisplayName("Should throw exception when status is lowercase")
        void testValidateStatus_LowercaseStatus() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateStatus("approved"));

            assertTrue(exception.getMessage().contains("Недопустимый статус"));
        }
    }

    // ==================== ТЕСТЫ ДЛЯ validateCommentLength ====================

    @Nested
    @DisplayName("validateCommentLength Tests")
    class ValidateCommentLengthTests {

        @Test
        @DisplayName("Should not throw exception when comment is null")
        void testValidateCommentLength_NullComment() {
            assertDoesNotThrow(() -> validator.validateCommentLength(null));
        }

        @Test
        @DisplayName("Should not throw exception when comment length is exactly 500")
        void testValidateCommentLength_ExactLimit() {
            String comment = "a".repeat(500);
            assertDoesNotThrow(() -> validator.validateCommentLength(comment));
        }

        @Test
        @DisplayName("Should not throw exception when comment length is less than 500")
        void testValidateCommentLength_ValidComment() {
            String comment = "Short comment";
            assertDoesNotThrow(() -> validator.validateCommentLength(comment));
        }

        @Test
        @DisplayName("Should throw exception when comment length exceeds 500")
        void testValidateCommentLength_TooLong() {
            String comment = "a".repeat(501);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateCommentLength(comment));

            assertTrue(exception.getMessage().contains("превышает 500 символов"));
            assertTrue(exception.getMessage().contains("501"));
        }
    }

    // ==================== ТЕСТЫ ДЛЯ validateResponseDate ====================

    @Nested
    @DisplayName("validateResponseDate Tests")
    class ValidateResponseDateTests {

        @Test
        @DisplayName("Should not throw exception when responseDate is null")
        void testValidateResponseDate_NullDate() {
            assertDoesNotThrow(() -> validator.validateResponseDate(null, 1L));
        }

        @Test
        @DisplayName("Should throw exception when responseDate is in future")
        void testValidateResponseDate_FutureDate() {
            LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateResponseDate(futureDate, 1L));

            assertTrue(exception.getMessage().contains("не может быть в будущем"));
        }

        @Test
        @DisplayName("Should throw exception when responseDate is before appealDate")
        void testValidateResponseDate_BeforeAppealDate() {
            LocalDateTime responseDate = LocalDateTime.now().minusDays(1);
            LocalDateTime appealDate = LocalDateTime.now().minusDays(2);

            when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.of(validApproval));
            validApproval.setAppealDate(appealDate);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateResponseDate(responseDate, 1L));

            assertTrue(exception.getMessage().contains("не может быть раньше"));
        }

        @Test
        @DisplayName("Should not throw exception when responseDate is after appealDate")
        void testValidateResponseDate_ValidDate() {
            LocalDateTime responseDate = LocalDateTime.now().minusDays(1);
            LocalDateTime appealDate = LocalDateTime.now().minusDays(3);

            when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.of(validApproval));
            validApproval.setAppealDate(appealDate);

            assertDoesNotThrow(() -> validator.validateResponseDate(responseDate, 1L));
        }

        @Test
        @DisplayName("Should throw exception when approval not found for date validation")
        void testValidateResponseDate_ApprovalNotFound() {
            when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateResponseDate(LocalDateTime.now().minusDays(1), 1L));

            assertEquals("Approval не найден для проверки дат", exception.getMessage());
        }
    }

    // ==================== ТЕСТЫ ДЛЯ validateAll ====================

    @Nested
    @DisplayName("validateAll Tests")
    class ValidateAllTests {

        @Test
        @DisplayName("Should not throw exception when all validations pass")
        void testValidateAll_ValidDto() {
            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(anyLong(), anyString(), anyString()))
                    .thenReturn(false);
            when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.of(validApproval));
            validApproval.setAppealDate(LocalDateTime.now().minusDays(2));

            assertDoesNotThrow(() -> validator.validateAll(validDto));
        }

        @Test
        @DisplayName("Should throw exception when approval not found")
        void testValidateAll_ApprovalNotFound() {
            when(approvalRepository.existsById(1L)).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertTrue(exception.getMessage().contains("Связанное соглашение не найдено"));
        }

        @Test
        @DisplayName("Should throw exception when duplicate exists")
        void testValidateAll_DuplicateExists() {
            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(anyLong(), anyString(), anyString()))
                    .thenReturn(true);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertTrue(exception.getMessage().contains("Duplicate"));
        }

        @Test
        @DisplayName("Should throw exception when status is invalid")
        void testValidateAll_InvalidStatus() {
            validDto.setStatus("INVALID");

            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(anyLong(), anyString(), anyString()))
                    .thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertTrue(exception.getMessage().contains("Недопустимый статус"));
        }

        @Test
        @DisplayName("Should throw exception when comment is too long")
        void testValidateAll_CommentTooLong() {
            validDto.setComment("a".repeat(501));

            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(anyLong(), anyString(), anyString()))
                    .thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertTrue(exception.getMessage().contains("превышает 500 символов"));
        }

        @Test
        @DisplayName("Should throw exception when responseDate is before appealDate")
        void testValidateAll_ResponseDateBeforeAppealDate() {
            validDto.setResponseDate(LocalDateTime.now().minusDays(1));

            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(anyLong(), anyString(), anyString()))
                    .thenReturn(false);
            when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.of(validApproval));
            validApproval.setAppealDate(LocalDateTime.now().minusDays(2)); // appealDate позже responseDate

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertTrue(exception.getMessage().contains("не может быть раньше"));
        }

        @Test
        @DisplayName("Should throw exception when responseDate is in future")
        void testValidateAll_ResponseDateInFuture() {
            validDto.setResponseDate(LocalDateTime.now().plusDays(1));

            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(anyLong(), anyString(), anyString()))
                    .thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertTrue(exception.getMessage().contains("не может быть в будущем"));
        }
    }
}