package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.dto.ProductReceiptDto;
import store.dto.Receipt;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptConverterTest {

    private final ReceiptConverter converter = new ReceiptConverter();
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        List<OrderResult> orderResults = List.of(
                new OrderResult("콜라", 3, 1, 0, 1000),
                new OrderResult("에너지바", 0, 0, 5, 2000)
        );
        receipt = converter.convertToReceipt(orderResults, true);
    }

    @DisplayName("구매한 모든 상품을 포함한 목록이 올바르게 변환된다.")
    @Test
    void 구매_상품_목록_변환_테스트() {
        assertThat(receipt.getProducts()).hasSize(2);
        assertThat(receipt.getProducts())
                .extracting(ProductReceiptDto::name)
                .containsExactly("콜라", "에너지바");

        assertThat(receipt.getProducts())
                .extracting(ProductReceiptDto::quantity)
                .containsExactly(3, 5);

        assertThat(receipt.getProducts())
                .extracting(ProductReceiptDto::price)
                .containsExactly(3000, 10000);
    }

    @DisplayName("증정된 상품 목록이 올바르게 변환된다.")
    @Test
    void 증정된_상품_목록_변환_테스트() {
        assertThat(receipt.getFreeProducts()).hasSize(1);
        assertThat(receipt.getFreeProducts().getFirst().name()).isEqualTo("콜라");
        assertThat(receipt.getFreeProducts().getFirst().quantity()).isEqualTo(1);
    }

    @DisplayName("할인이 적용되기 전, 총 금액 및 수량이 올바르게 계산된다.")
    @Test
    void 총_금액_및_수량_계산_테스트() {
        assertThat(receipt.getTotalOriginInfo().quantity()).isEqualTo(8);
        assertThat(receipt.getTotalOriginInfo().price()).isEqualTo(13000);
    }

    @DisplayName("총 행사할인 금액이 올바르게 계산된다.")
    @Test
    void 총_행사할인_금액_계산_테스트() {
        assertThat(receipt.getTotalFreePrice()).isEqualTo(1000);
    }

    @DisplayName("멤버십 할인 금액이 올바르게 계산된다.")
    @Test
    void 멤버십_할인_금액_계산_테스트() {
        assertThat(receipt.getMembershipPrice()).isEqualTo(3000);
    }

    @DisplayName("최종 결제 금액이 올바르게 계산된다.")
    @Test
    void 최종_결제_금액_계산_테스트() {
        assertThat(receipt.getFinalPayment()).isEqualTo(9000);
    }
}
