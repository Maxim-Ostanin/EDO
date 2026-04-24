package edo_service;

import java.util.List;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.Approval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


    @Component
    @RequiredArgsConstructor
    public class AdditionalApprovalValidator {

        private final AdditionalApprovalRepository additionalApprovalRepository;
        private final ApprovalRepository approvalRepository;

        public AdditionalApprovalValidator(AdditionalApprovalRepository additionalApprovalRepository, ApprovalRepository approvalRepository) {
            this.additionalApprovalRepository = additionalApprovalRepository;
            this.approvalRepository = approvalRepository;
        }

        // 1. Валидация на дубликат (идемпотентность)
        public void validateNoDuplicate(Long approvalId, String type, String status) {
            if (additionalApprovalRepository.existsByApprovalIdAndTypeAndStatus(approvalId, type, status)) {
                throw new RuntimeException(
                        String.format("Duplicate: дополнительное согласование уже существует для approvalId=%d, type=%s, status=%s",
                                approvalId, type, status)
                );
            }
        }

        // 2. Валидация существования связанного соглашения
        public void validateApprovalExists(Long approvalId) {
            if (approvalId == null) {
                throw new RuntimeException("approvalId не может быть null");
            }
            if (!approvalRepository.existsById(approvalId)) {
                throw new RuntimeException("Связанное соглашение не найдено: approvalId=" + approvalId);
            }
        }

        // 3. Валидация статуса
        public void validateStatus(String status) {
            if (status == null) return;

            List<String> allowed = List.of("APPROVED", "REJECTED", "PENDING");
            if (!allowed.contains(status)) {
                throw new RuntimeException(
                        "Недопустимый статус: '" + status + "'. Допустимые значения: " + allowed
                );
            }
        }

        // 4. Валидация длины комментария
        public void validateCommentLength(String comment) {
            if (comment != null && comment.length() > 500) {
                throw new RuntimeException(
                        "comment превышает 500 символов (текущая длина: " + comment.length() + ")"
                );
            }
        }

        // 5. Валидация даты ответа
        public void validateResponseDate(LocalDateTime responseDate, Long approvalId) {
            if (responseDate == null) return;

            // Не позже текущего момента
            LocalDateTime now = LocalDateTime.now();
            if (responseDate.isAfter(now)) {
                throw new RuntimeException(
                        "responseDate не может быть в будущем: " + responseDate + " > " + now
                );
            }

            // Не раньше appealDate связанного соглашения
            Approval approval = approvalRepository.findById(approvalId)
                    .orElseThrow(() -> new RuntimeException("Approval не найден для проверки дат"));

            LocalDateTime appealDate = approval.getAppealDate(); // убедитесь, что поле существует
            if (appealDate != null && responseDate.isBefore(appealDate)) {
                throw new RuntimeException(
                        String.format("responseDate (%s) не может быть раньше appealDate (%s)",
                                responseDate, appealDate)
                );
            }
        }

        // Комбинированный метод для всех проверок
        public void validateAll(AdditionalApprovalDto dto) {
            validateApprovalExists(dto.getApprovalId());
            validateNoDuplicate(dto.getApprovalId(), dto.getType(), dto.getStatus());
            validateStatus(dto.getStatus());
            validateCommentLength(dto.getComment());
            validateResponseDate(dto.getResponseDate(), dto.getApprovalId());
        }
    }

