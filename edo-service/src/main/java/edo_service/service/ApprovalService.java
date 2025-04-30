package edo_service.service;

import common.dto.ApprovalDto;
import edo_repository.entity.Appeal;
import edo_repository.entity.Approval;
import edo_repository.entity.enums.ApprovalStatus;
import edo_repository.repository.AppealRepository;
import edo_repository.repository.ApprovalRepository;
import edo_service.exception.ApprovalValidationException;
import edo_service.mapper.ApprovalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalService.class);
    private final ApprovalMapper approvalMapper = ApprovalMapper.INSTANCE;
    private final ApprovalRepository approvalRepository;
    private final AppealRepository appealRepository;

    public ApprovalService(ApprovalRepository approvalRepository, AppealRepository appealRepository) {
        this.approvalRepository = approvalRepository;
        this.appealRepository = appealRepository;
    }

    public ApprovalDto createApproval(ApprovalDto approvalDto) {
        logger.info("Запрос на создание согласования: {}", approvalDto);

        if (approvalRepository.findByAppealIdAndStatusAndResponseDate(
                approvalDto.getAppealId(),
                approvalDto.getStatus().name(),
                approvalDto.getResponseDate()).isPresent()) {

            logger.warn("Ошибка: согласование уже существует для appealId={} и status={}",
                    approvalDto.getAppealId(), approvalDto.getStatus().name());

            throw new ApprovalValidationException("Такое согласование уже существует");
        }

        Appeal appeal = appealRepository.findById(approvalDto.getAppealId())
                .orElseThrow(() -> {

                    logger.error("Ошибка: обращение с id={} не найдено", approvalDto.getAppealId());

                    return new ApprovalValidationException("Обращение не найдено");
                });

        if (approvalDto.getResponseDate().isBefore(appeal.getCreateDate())) {

            logger.warn("Ошибка: responseDate={} раньше appealDate={}",
                    approvalDto.getResponseDate(), appeal.getCreateDate());

            throw new ApprovalValidationException("Дата ответа не может быть раньше даты обращения!");
        }

        Approval approval = approvalMapper.toEntity(approvalDto);
        approval.setAppeal(appeal);
        Approval savedApproval = approvalRepository.save(approval);

        logger.info("Согласование успешно сохранено: {}", savedApproval);

        return approvalMapper.toDto(savedApproval);
    }

    public ApprovalDto getApprovalById(Long id) {
        logger.info("Запрос на получение согласования с id={}", id);

        return approvalRepository.findById(id)
                .map(approvalMapper::toDto)
                .orElseThrow(() -> {

                    logger.error("Ошибка: согласование с id={}", id);

                    return new ApprovalValidationException("Согласование не найдено");
                });
    }

    public List<ApprovalDto> getAllApprovals() {
        logger.info("Запрос на получение всех согласований");

        return approvalRepository.findAll().stream()
                .map(approvalMapper::toDto)
                .collect(Collectors.toList());
    }

    public ApprovalDto updateApproval(Long id, ApprovalDto approvalDto) {
        logger.info("Запрос на обновление согласований с id={}", id);

        Approval existingApproval = approvalRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Ошибка: согласование с id={} не найдено", id);

                    return new ApprovalValidationException("Согласование не найдено");
                });

        existingApproval.setStatus(ApprovalStatus.valueOf(approvalDto.getStatus().name()));
        existingApproval.setComment(approvalDto.getComment());
        existingApproval.setResponseDate(approvalDto.getResponseDate());

        Approval updatedApproval = approvalRepository.save(existingApproval);

        logger.info("Согласование успешно обновлено: {}", updatedApproval);

        return approvalMapper.toDto(updatedApproval);
    }

    public void deleteApproval(Long id) {
        logger.info("Запрос на удаление согласования с id={}", id);


        try {
            approvalRepository.deleteById(id);

            logger.info("Согласование успешно удалено: id={}", id);
        } catch (Exception e) {

            logger.error("Ошибка при удалении согласования с id={}: {}", id, e.getMessage(), e);

            throw e;
        }
    }

}
