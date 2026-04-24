package edo_service;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private Approval mockApproval;

    @BeforeEach
    void setUp() {
        validDto = new AdditionalApprovalDto();
        validDto.setApprovalId(1L);
        validDto.setType("POST_APPROVAL");
        validDto.setStatus("PENDING");
        validDto.setComment("Test comment");
        validDto.setResponseDate(LocalDateTime.now());

        mockApproval = new Approval();
        mockApproval.setId(1L);
        mockApproval.setAppealDate(LocalDateTime.now().minusDays(1));
    }

    // ==================== TESTS FOR validateApprovalExists ====================

    @Nested
    @DisplayName("validateApprovalExists Tests")
    class ValidateApprovalExistsTests {

        @Test
        @DisplayName("Should pass when approval exists")
        void testValidateApprovalExists_Valid() {
            when(approvalRepository.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> validator.validateApprovalExists(1L));
            verify(approvalRepository).existsById(1L);
        }

        @Test
        @DisplayName("Should throw exception when approvalId is null")
        void testValidateApprovalExists_NullApprovalId() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateApprovalExists(null));

            assertEquals("approvalId не может быть null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when approval does not exist")
        void testValidateApprovalExists_ApprovalNotFound() {
            when(approvalRepository.existsById(999L)).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateApprovalExists(999L));

            assertEquals("Связанное соглашение не найдено: approvalId=999", exception.getMessage());
            verify(approvalRepository).existsById(999L);
        }
    }

    // ==================== TESTS FOR validateNoDuplicate ====================

    @Nested
    @DisplayName("validateNoDuplicate Tests")
    class ValidateNoDuplicateTests {

        @Test
        @DisplayName("Should pass when no duplicate exists")
        void testValidateNoDuplicate_NoDuplicate() {
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "POST_APPROVAL", "PENDING"))
                    .thenReturn(false);

            assertDoesNotThrow(() -> validator.validateNoDuplicate(1L, "POST_APPROVAL", "PENDING"));
            verify(additionalApprovalRepository).existsByApprovalIdAndTypeAndStatus(1L, "POST_APPROVAL", "PENDING");
        }

        @Test
        @DisplayName("Should throw exception when duplicate exists")
        void testValidateNoDuplicate_DuplicateExists() {
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "POST_APPROVAL", "PENDING"))
                    .thenReturn(true);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateNoDuplicate(1L, "POST_APPROVAL", "PENDING"));

            assertTrue(exception.getMessage().contains("Duplicate"));
            assertTrue(exception.getMessage().contains("approvalId=1"));
            assertTrue(exception.getMessage().contains("type=POST_APPROVAL"));
            assertTrue(exception.getMessage().contains("status=PENDING"));
        }

        @Test
        @DisplayName("Should pass when duplicate check with different parameters")
        void testValidateNoDuplicate_DifferentParameters() {
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "PRE_APPROVAL", "APPROVED"))
                    .thenReturn(false);

            assertDoesNotThrow(() -> validator.validateNoDuplicate(1L, "PRE_APPROVAL", "APPROVED"));
        }
    }

    // ==================== TESTS FOR validateStatus ====================

    @Nested
    @DisplayName("validateStatus Tests")
    class ValidateStatusTests {

        @Test
        @DisplayName("Should pass when status is APPROVED")
        void testValidateStatus_Approved() {
            assertDoesNotThrow(() -> validator.validateStatus("APPROVED"));
        }

        @Test
        @DisplayName("Should pass when status is REJECTED")
        void testValidateStatus_Rejected() {
            assertDoesNotThrow(() -> validator.validateStatus("REJECTED"));
        }

        @Test
        @DisplayName("Should pass when status is PENDING")
        void testValidateStatus_Pending() {
            assertDoesNotThrow(() -> validator.validateStatus("PENDING"));
        }

        @Test
        @DisplayName("Should pass when status is null")
        void testValidateStatus_NullStatus() {
            assertDoesNotThrow(() -> validator.validateStatus(null));
        }

        @Test
        @DisplayName("Should throw exception when status is invalid")
        void testValidateStatus_InvalidStatus() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateStatus("INVALID_STATUS"));

            assertTrue(exception.getMessage().contains("Недопустимый статус"));
            assertTrue(exception.getMessage().contains("INVALID_STATUS"));
            assertTrue(exception.getMessage().contains("APPROVED, REJECTED, PENDING"));
        }

        @Test
        @DisplayName("Should throw exception when status is empty string")
        void testValidateStatus_EmptyStatus() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateStatus(""));

            assertTrue(exception.getMessage().contains("Недопустимый статус"));
        }

        @Test
        @DisplayName("Should throw exception when status is lowercase")
        void testValidateStatus_LowercaseStatus() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateStatus("pending"));

            assertTrue(exception.getMessage().contains("Недопустимый статус"));
        }
    }

    // ==================== TESTS FOR validateCommentLength ====================

    @Nested
    @DisplayName("validateCommentLength Tests")
    class ValidateCommentLengthTests {

        @Test
        @DisplayName("Should pass when comment is null")
        void testValidateCommentLength_NullComment() {
            assertDoesNotThrow(() -> validator.validateCommentLength(null));
        }

        @Test
        @DisplayName("Should pass when comment is empty")
        void testValidateCommentLength_EmptyComment() {
            assertDoesNotThrow(() -> validator.validateCommentLength(""));
        }

        @Test
        @DisplayName("Should pass when comment is exactly 500 characters")
        void testValidateCommentLength_Exactly500Chars() {
            String comment = "a".repeat(500);
            assertDoesNotThrow(() -> validator.validateCommentLength(comment));
        }

        @Test
        @DisplayName("Should pass when comment is less than 500 characters")
        void testValidateCommentLength_LessThan500Chars() {
            String comment = "a".repeat(499);
            assertDoesNotThrow(() -> validator.validateCommentLength(comment));
        }

        @Test
        @DisplayName("Should throw exception when comment exceeds 500 characters")
        void testValidateCommentLength_Exceeds500Chars() {
            String comment = "a".repeat(501);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateCommentLength(comment));

            assertTrue(exception.getMessage().contains("comment превышает 500 символов"));
            assertTrue(exception.getMessage().contains("501"));
        }

        @Test
        @DisplayName("Should throw exception when comment exceeds 500 characters significantly")
        void testValidateCommentLength_Exceeds500CharsByLargeMargin() {
            String comment = "a".repeat(1000);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateCommentLength(comment));

            assertTrue(exception.getMessage().contains("1000"));
        }
    }

    // ==================== TESTS FOR validateResponseDate ====================

    @Nested
    @DisplayName("validateResponseDate Tests")
    class ValidateResponseDateTests {

        @Test
        @DisplayName("Should pass when responseDate is null")
        void testValidateResponseDate_NullResponseDate() {
            assertDoesNotThrow(() -> validator.validateResponseDate(null, 1L));
            verify(approvalRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should pass when responseDate is now")
        void testValidateResponseDate_CurrentTime() {
            when(approvalRepository.findById(1L)).thenReturn(Optional.of(mockApproval));

            assertDoesNotThrow(() -> validator.validateResponseDate(LocalDateTime.now(), 1L));
        }

        @Test
        @DisplayName("Should pass when responseDate is in past")
        void testValidateResponseDate_PastDate() {
            LocalDateTime pastDate = LocalDateTime.now().minusDays(5);
            when(approvalRepository.findById(1L)).thenReturn(Optional.of(mockApproval));

            assertDoesNotThrow(() -> validator.validateResponseDate(pastDate, 1L));
        }

        @Test
        @DisplayName("Should throw exception when responseDate is in future")
        void testValidateResponseDate_FutureDate() {
            LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateResponseDate(futureDate, 1L));

            assertTrue(exception.getMessage().contains("responseDate не может быть в будущем"));
        }

        @Test
        @DisplayName("Should pass when responseDate is after appealDate")
        void testValidateResponseDate_AfterAppealDate() {
            LocalDateTime responseDate = LocalDateTime.now();
            mockApproval.setAppealDate(responseDate.minusDays(2));
            when(approvalRepository.findById(1L)).thenReturn(Optional.of(mockApproval));

            assertDoesNotThrow(() -> validator.validateResponseDate(responseDate, 1L));
        }

        @Test
        @DisplayName("Should pass when responseDate equals appealDate")
        void testValidateResponseDate_EqualsAppealDate() {
            LocalDateTime sameDate = LocalDateTime.now();
            mockApproval.setAppealDate(sameDate);
            when(approvalRepository.findById(1L)).thenReturn(Optional.of(mockApproval));

            assertDoesNotThrow(() -> validator.validateResponseDate(sameDate, 1L));
        }

        @Test
        @DisplayName("Should throw exception when responseDate is before appealDate")
        void testValidateResponseDate_BeforeAppealDate() {
            LocalDateTime appealDate = LocalDateTime.now();
            LocalDateTime responseDate = appealDate.minusDays(1);
            mockApproval.setAppealDate(appealDate);
            when(approvalRepository.findById(1L)).thenReturn(Optional.of(mockApproval));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateResponseDate(responseDate, 1L));

            assertTrue(exception.getMessage().contains("responseDate"));
            assertTrue(exception.getMessage().contains("не может быть раньше"));
        }

        @Test
        @DisplayName("Should throw exception when approval not found during date validation")
        void testValidateResponseDate_ApprovalNotFound() {
            when(approvalRepository.findById(999L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateResponseDate(LocalDateTime.now(), 999L));

            assertEquals("Approval не найден для проверки дат", exception.getMessage());
        }

        @Test
        @DisplayName("Should pass when appealDate is null")
        void testValidateResponseDate_AppealDateNull() {
            mockApproval.setAppealDate(null);
            when(approvalRepository.findById(1L)).thenReturn(Optional.of(mockApproval));

            assertDoesNotThrow(() -> validator.validateResponseDate(LocalDateTime.now(), 1L));
        }
    }

    // ==================== TESTS FOR validateAll (Combined) ====================

    @Nested
    @DisplayName("validateAll Combined Tests")
    class ValidateAllTests {

        @Test
        @DisplayName("Should pass when all validations pass")
        void testValidateAll_ValidDto() {
            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "POST_APPROVAL", "PENDING"))
                    .thenReturn(false);
            when(approvalRepository.findById(1L)).thenReturn(Optional.of(mockApproval));

            assertDoesNotThrow(() -> validator.validateAll(validDto));
        }

        @Test
        @DisplayName("Should throw exception when approval not found")
        void testValidateAll_ApprovalNotFound() {
            validDto.setApprovalId(999L);
            when(approvalRepository.existsById(999L)).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertEquals("Связанное соглашение не найдено: approvalId=999", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when duplicate exists")
        void testValidateAll_DuplicateExists() {
            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "POST_APPROVAL", "PENDING"))
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
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "POST_APPROVAL", "INVALID"))
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
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "POST_APPROVAL", "PENDING"))
                    .thenReturn(false);
            when(approvalRepository.findById(1L)).thenReturn(Optional.of(mockApproval));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertTrue(exception.getMessage().contains("comment превышает 500 символов"));
        }

        @Test
        @DisplayName("Should throw exception when responseDate is in future")
        void testValidateAll_ResponseDateInFuture() {
            validDto.setResponseDate(LocalDateTime.now().plusDays(1));
            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "POST_APPROVAL", "PENDING"))
                    .thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertTrue(exception.getMessage().contains("responseDate не может быть в будущем"));
        }

        @Test
        @DisplayName("Should throw exception when responseDate is before appealDate")
        void testValidateAll_ResponseDateBeforeAppealDate() {
            LocalDateTime appealDate = LocalDateTime.now();
            validDto.setResponseDate(appealDate.minusDays(1));
            mockApproval.setAppealDate(appealDate);

            when(approvalRepository.existsById(1L)).thenReturn(true);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, "POST_APPROVAL", "PENDING"))
                    .thenReturn(false);
            when(approvalRepository.findById(1L)).thenReturn(Optional.of(mockApproval));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> validator.validateAll(validDto));

            assertTrue(exception.getMessage().contains("не может быть раньше"));
        }
    }

    // ==================== EDGE CASES ====================

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null DTO in validateAll")
        void testValidateAll_NullDto() {
            assertThrows(NullPointerException.class, () -> validator.validateAll(null));
        }

        @Test
        @DisplayName("Should handle very long type string")
        void testValidateNoDuplicate_VeryLongType() {
            String veryLongType = "A".repeat(1000);
            when(additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(1L, veryLongType, "PENDING"))
                    .thenReturn(false);

            assertDoesNotThrow(() -> validator.validateNoDuplicate(1L, veryLongType, "PENDING"));
        }

        @Test
        @DisplayName("Should handle special characters in comment")
        void testValidateCommentLength_SpecialCharacters() {
            String commentWithSpecialChars = "!@#$%^&*()_+{}|:<>?~`-=[]\\;',./\"'";
            assertDoesNotThrow(() -> validator.validateCommentLength(commentWithSpecialChars));
        }

        @Test
        @DisplayName("Should handle Unicode characters in comment")
        void testValidateCommentLength_UnicodeCharacters() {
            String unicodeComment = "Привет мир 你好 세계 こんにちは";
            assertDoesNotThrow(() -> validator.validateCommentLength(unicodeComment));
        }
    }
}