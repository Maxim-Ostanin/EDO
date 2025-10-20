package edo_service.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleTest {

    @Test
    void simplestTest() {
        System.out.println("=== ТЕСТ ЗАПУЩЕН ===");
        assertTrue(true, "Простейший тест должен проходить");
        System.out.println("=== ТЕСТ ПРОЙДЕН ===");
    }

    @Test
    void anotherSimpleTest() {
        int result = 2 + 2;
        assertEquals(4, result, "2 + 2 должно быть 4");
    }
}