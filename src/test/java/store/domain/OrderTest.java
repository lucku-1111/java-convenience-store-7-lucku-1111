package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    @DisplayName("들어온 값만큼 수량을 올바르게 변경한다.")
    @ParameterizedTest(name = "수량 변경 값: {0}")
    @ValueSource(ints = {5, -3, 0})
    void 주문_수량_변경_테스트(int quantityDelta) {
        Order order = new Order("콜라", 10, DateTimes.now().toLocalDate());

        order.updateQuantity(quantityDelta);

        assertThat(order.getQuantity()).isEqualTo(10 + quantityDelta);
    }
}
