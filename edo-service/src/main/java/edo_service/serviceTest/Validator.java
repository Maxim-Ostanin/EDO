package edo_service.serviceTest;


import common.dto.AdditionalApprovalDto;
import edo_repository.entity.Approval;
import edo_repository.entity.enums.ApprovalStatus;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_repository.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Validator {

    private final AdditionalApprovalRepository additionalApprovalRepository;
    private final ApprovalRepository approvalRepository;

    public void validate(AdditionalApprovalDto dto) {
        Approval approval = approvalRepository.findById(dto.getApprovalId())
                .orElseThrow(() -> new RuntimeException("Ошибка: Соглашение с ID " +
                         dto.getApprovalId() + " не найдено!"));
        validateFields(dto);
        validateFieldsLength (dto);
        validateRelations(dto);
        validateDateIsNotBeforeApproval(dto.getResponseDate(), approval.getResponseDate());
        validateDateIsNotInFuture(dto.getResponseDate());
        validateDuplicate(dto);
    }


    private void validateFields(AdditionalApprovalDto dto) {
        try {
            ApprovalStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Статус может быть только: APPROVED, REJECTED, PENDING");
        }
    }


    private void validateFieldsLength(AdditionalApprovalDto dto) {
        if (dto.getComment() != null && dto.getComment().length() > 500) {
            throw new RuntimeException("Комментарий превысил 500 символов!");
        }

    }

    private void validateRelations(AdditionalApprovalDto dto) {
        Approval approval = approvalRepository.findById(dto.getApprovalId())
                .orElseThrow(() ->
                        new RuntimeException("Ошибка: Соглашение с ID " +
                                dto.getApprovalId() + " не найдено!"));
        }


    private void validateDateIsNotBeforeApproval(LocalDateTime additionalDate, LocalDateTime approvalDate) {
        if (additionalDate.isBefore(approvalDate)) {
            throw new RuntimeException("Дата доп.согласования не может быть раньше даты заключения");
        }
    }

    private void validateDateIsNotInFuture(LocalDateTime date) {
        if (date.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Дата не может быть в будущем!");
        }
    }


    private void validateDuplicate(AdditionalApprovalDto dto) {
        boolean exists = additionalApprovalRepository.existsByApprovalIdAndType(dto.getApprovalId(),dto.getType());

        if (exists) {
            throw new RuntimeException("Такое соглашение с типом: " + dto.getType() + " уже зарегистрировано");
        }
    }
}



