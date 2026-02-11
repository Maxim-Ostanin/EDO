package edo_service.ServiceTest;


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

    //Главный метод
    public void validate(AdditionalApprovalDto dto) {
        Approval approval = approvalRepository.findById(dto.getApprovalId())
                .orElseThrow(() -> new RuntimeException("Ошибка: Соглашение с ID " +
                         dto.getApprovalId() + " не найдено!"));
        validateFields(dto);             // 1. Простые поля
        validateFieldsLength (dto);      // 2. Длина комментария
        validateRelations(dto);          // 3.1 Проверка наличия
        validateDateIsNotBeforeApproval(dto.getResponseDate(), approval.getResponseDate()); // 3.2.1 Проверка хронологии
        validateDateIsNotInFuture(dto.getResponseDate()); //3.2.2 Проверка на будущее
        validateDuplicate(dto);          // 4. Проверка на дубликат
    }

    // 1. Метод проверки полей
    private void validateFields(AdditionalApprovalDto dto) {
        try {
            ApprovalStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Статус может быть только: APPROVED, REJECTED, PENDING");
        }
    }


    // 2.Метод проверки поля комментарий
    private void validateFieldsLength(AdditionalApprovalDto dto) {
        if (dto.getComment() != null && dto.getComment().length() > 500) {
            throw new RuntimeException("Комментарий превысил 500 символов!");
        }

    }

    // 3.1 Проверка наличия связанного соглашения
    private void validateRelations(AdditionalApprovalDto dto) {
        Approval approval = approvalRepository.findById(dto.getApprovalId())
                .orElseThrow(() ->
                        new RuntimeException("Ошибка: Соглашение с ID " +
                                dto.getApprovalId() + " не найдено!"));
        }


    // 3.2.1 Проверка хронологии
    private void validateDateIsNotBeforeApproval(LocalDateTime additionalDate, LocalDateTime approvalDate) {
        if (additionalDate.isBefore(approvalDate)) {
            throw new RuntimeException("Дата доп.согласования не может быть раньше даты заключения");
        }
    }

    // 3.2.2 Проверка на будущее
    private void validateDateIsNotInFuture(LocalDateTime date) {
        if (date.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Дата не может быть в будущем!");
        }
    }


    // 4. Проверка дубликатов
    private void validateDuplicate(AdditionalApprovalDto dto) {
        boolean exists = additionalApprovalRepository.existsByApprovalIdAndType(dto.getApprovalId(),dto.getType());

        if (exists) {
            throw new RuntimeException("Такое соглашение с типом: " + dto.getType() + " уже зарегистрировано");
        }
    }
}



