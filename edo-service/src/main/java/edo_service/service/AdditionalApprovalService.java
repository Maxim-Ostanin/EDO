package edo_service.service;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_service.serviceTest.Validator;
import edo_service.converter.AdditionalApprovalConverterDtoAndAddApp;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdditionalApprovalService {

    private final AdditionalApprovalRepository additionalApprovalRepository;
    private final AdditionalApprovalConverterDtoAndAddApp converter;
    private final Validator validator;

    @Transactional
    public void saveAddApproval (AdditionalApprovalDto dto) {

        log.info("Начало процесса сохранения доп. соглашения для основного ID: {}", dto.getApprovalId());

        try {
            validator.validate(dto);
            AdditionalApproval additionalApproval = converter.toAddApp(dto);
            AdditionalApproval savedEntity = additionalApprovalRepository.save(additionalApproval);
            AdditionalApprovalDto additionalApprovalDto = converter.toDto(savedEntity);

            log.info("Доп. соглашение успешно сохранено. Тип: {}, ID: {}", dto.getType(), dto.getId());

        } catch (Exception e) {
            log.error("Не удалось сохранить доп. соглашение для ID: {}. Причина: {}",
                    dto.getApprovalId(), e.getMessage());
            throw e;
        }
    }

    public AdditionalApprovalDto findById (Long id) {

            log.info("Начало процесса получения доп. соглашения с id: {}", id);

        try {
            AdditionalApproval entity = additionalApprovalRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Дополнительное согласование с ID " + id + " не найдено"));
            AdditionalApprovalDto responseDto = converter.toDto(entity);

            log.info("Доп. соглашение успешно получено с id: {}", id);
            return responseDto;

        } catch (Exception e) {
            log.error("Не удалось получить доп. соглашение для ID: {}. Причина: {}",
                    id, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void deleteById (Long id) {
        log.warn("Запрос на удаление доп. соглашения с id: {}", id);
        try {
            if (!additionalApprovalRepository.existsById(id)) {
                log.error("Удаление невозможно: запись с id {} не существует", id);
                throw new RuntimeException("Запись не найдена");
            }
            additionalApprovalRepository.deleteById(id);
            log.info("Доп. соглашение с id: {} успешно удалено из базы данных", id);

        } catch (Exception e) {
            log.error("Ошибка при удалении записи id: {}. Причина: {}", id, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public AdditionalApprovalDto editAddApproval (Long id, AdditionalApprovalDto addApprovalDto) {

        log.info("Начало редактирования доп. соглашения с id: {}. Новые данные: {}", id, addApprovalDto);
        try {
            validator.validate(addApprovalDto);
            AdditionalApproval entity = additionalApprovalRepository.findById(id).orElseThrow(()
                    -> new RuntimeException("Дополнительное согласование с ID " + id + " не найдено"));
            AdditionalApproval updatedEntity = converter.toAddApp(addApprovalDto);
            updatedEntity.setId(id);
            AdditionalApproval saved = additionalApprovalRepository.save(updatedEntity);
            AdditionalApprovalDto dto = converter.toDto(saved);

            log.info("Доп. соглашение с id: {} успешно обновлено", id);
            return dto;

        } catch  (Exception e) {
            log.error("Не удалось обновить доп. соглашение с id: {}. Причина: {}", id, e.getMessage());
            throw e;
        }
    }
}
