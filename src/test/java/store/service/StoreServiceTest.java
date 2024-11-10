package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.ErrorMessages;
import store.dto.ProductDto;
import store.dto.PurchaseInfo;
import store.dto.Receipt;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreServiceTest {
    private StoreService storeService;

    @BeforeEach
    void setUp() throws IOException {
        TestStoreServiceConfig config = new TestStoreServiceConfig();
        storeService = config.storeService();
    }

    @DisplayName("상품 목록을 올바르게 반환한다.")
    @Test
    void 상품_목록_조회_테스트() {
        List<ProductDto> products = storeService.getProducts();

        assertThat(products).isNotEmpty();
    }

    @DisplayName("구매 정보를 적용하고 주문을 생성한다.")
    @Test
    void 구매_정보_적용_테스트() {
        List<PurchaseInfo> purchases = List.of(
                new PurchaseInfo("콜라", 3),
                new PurchaseInfo("사이다", 2)
        );

        storeService.applyPurchaseInfo(purchases);

        assertThat(storeService.hasNextOrder()).isTrue();
    }

    @DisplayName("주문 계산 결과를 반환한다.")
    @Test
    void 주문_계산_테스트() {
        List<PurchaseInfo> purchases = List.of(
                new PurchaseInfo("콜라", 3),
                new PurchaseInfo("사이다", 2)
        );

        storeService.applyPurchaseInfo(purchases);
        Receipt receipt = storeService.calculateOrders(true);

        assertThat(receipt).isNotNull();
        assertThat(receipt.getProducts()).isNotEmpty();
        assertThat(receipt.getFinalPayment()).isGreaterThan(0);
    }

    @DisplayName("존재하지 않는 상품을 입력하면 하면 예외를 발생시킨다.")
    @Test
    void 존재하지_않는_상품_예외_테스트() {
        List<PurchaseInfo> purchases = List.of(
                new PurchaseInfo("여자친구", 3)
        );

        assertThatThrownBy(() -> storeService.applyPurchaseInfo(purchases))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessages.NOT_EXIST_PRODUCT_MESSAGE);
    }

    @DisplayName("재고 수량을 초과하여 입력하면 하면 예외를 발생시킨다.")
    @Test
    void 재고_수량_초과_예외_테스트() {
        List<PurchaseInfo> purchases = List.of(
                new PurchaseInfo("콜라", 100)
        );

        assertThatThrownBy(() -> storeService.applyPurchaseInfo(purchases))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessages.QUANTITY_EXCEED_MESSAGE);
    }
}
