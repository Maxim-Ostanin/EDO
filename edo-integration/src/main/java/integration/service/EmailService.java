package integration.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendEmail(String to, String subject, String body) {
        // Заглушка для работы с email
        System.out.println("Email sent to " + to);
    }
}
