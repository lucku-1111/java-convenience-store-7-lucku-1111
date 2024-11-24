package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {
    @DisplayName("들어온 값만큼 상품 수량이 감소한다.")
    @Test
    void 상품_수량_감소_테스트() {
        Product product = new Product("콜라", 1000, 10, "2+1");
        product.soldQuantity(3);

        assertThat(product.getQuantity()).isEqualTo(7);
    }
}
