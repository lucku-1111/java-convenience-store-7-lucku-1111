package store.service;

import java.time.LocalDate;
import store.util.LocalDateGenerator;

public class TestLocalDateGenerator implements LocalDateGenerator {
    @Override
    public LocalDate today() {
        return LocalDate.of(2024, 8, 8);
    }
}
