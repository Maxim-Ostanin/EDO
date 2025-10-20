package edo_repository;

import edo_repository.repository.AdditionalApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication(
        exclude = {SecurityAutoConfiguration.class},
        scanBasePackages = {"edo_repository", "edo_public_api", "edo_service"}
)
public class RepositoryApplication {

    @Autowired
    private AdditionalApprovalRepository additionalApprovalRepository;

    public static void main(String[] args) {
        SpringApplication.run(RepositoryApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkAdditionalApprovalTable() {
        System.out.println("=== ПРОВЕРКА ТАБЛИЦЫ additional_approval ===");
        try {
            long count = additionalApprovalRepository.count();
            System.out.println("✅ Таблица additional_approval существует и доступна!");
            System.out.println("✅ Количество записей: " + count);
        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }
}