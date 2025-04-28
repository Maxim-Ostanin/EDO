package edo_service.service;

import common.dto.AppealDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppealService {
    public List<AppealDto> getAllAppeals() {

        return List.of(new AppealDto(1L, "Обращение 1", "Описание 1"));
    }
}
