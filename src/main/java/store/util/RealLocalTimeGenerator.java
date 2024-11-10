package store.util;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class RealLocalTimeGenerator implements LocalTimeGenerator {
    @Override
    public LocalDate today() {
        return DateTimes.now().toLocalDate();
    }
}
