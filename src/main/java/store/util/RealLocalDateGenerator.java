package store.util;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class RealLocalDateGenerator implements LocalDateGenerator {
    @Override
    public LocalDate today() {
        return DateTimes.now().toLocalDate();
    }
}
