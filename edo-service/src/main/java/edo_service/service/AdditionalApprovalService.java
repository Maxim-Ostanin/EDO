package edo_service.service;

import common.dto.AdditionalApprovalDto;
import edo_repository.entity.AdditionalApproval;
import edo_repository.repository.AdditionalApprovalRepository;
import edo_service.ServiceTest.Validator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdditionalApprovalService {

    private final AdditionalApprovalRepository additionalApprovalRepository;
    private final AdditionalApprovalToAdditionalApprovalDtoConverter converter;
    private final Validator validator;

    //Сохранение доп. согласования
    @Transactional
    public AdditionalApprovalDto saveAddApproval (AdditionalApprovalDto dto) {

        // Лог перед началом операции
        log.info("Начало процесса сохранения доп. соглашения для основного ID: {}", dto.getApprovalId());

        try {
            validator.validate(dto);
            AdditionalApproval additionalApproval = converter.toAddApp(dto);
            AdditionalApproval savedEntity = additionalApprovalRepository.save(additionalApproval);
            AdditionalApprovalDto additionalApprovalDto = converter.toDto(savedEntity);

            // Лог при успешном завершении
            log.info("Доп. соглашение успешно сохранено. Тип: {}, ID: {}", dto.getType(), savedEntity.getId());

            return additionalApprovalDto;

        } catch (Exception e) {
            // Лог при ошибке (неуспешное выполнение)
            log.error("Не удалось сохранить доп. соглашение для ID: {}. Причина: {}",
                    dto.getApprovalId(), e.getMessage());
            throw e;
        }
    }

    //Получене доп. согласования
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

    //Метод для удаления
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

    //Метод для редактирования доп.согл.
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
