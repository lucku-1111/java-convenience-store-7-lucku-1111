package store.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryTest {

    @DisplayName("프로모션 제품만 존재할 때 일반 제품이 추가된다.")
    @Test
    void 일반_제품_자동_추가_테스트() {
        Product promotionalProduct = new Product("콜라", 1000, 10, "탄산2+1");
        ProductRepository repository = new ProductRepository(List.of(promotionalProduct));

        Optional<Product> regularProduct = repository.findRegularProduct("콜라");

        assertThat(regularProduct).isPresent();
        assertThat(regularProduct.get().getName()).isEqualTo("콜라");
        assertThat(regularProduct.get().getQuantity()).isEqualTo(0);
        assertThat(regularProduct.get().getPromotion()).isEmpty();
    }

    @DisplayName("모든 상품을 반환한다.")
    @Test
    void 모든_제품_반환_테스트() {
        Product product1 = new Product("콜라", 1000, 10, "탄산2+1");
        Product product2 = new Product("사이다", 1000, 15, "");
        ProductRepository repository = new ProductRepository(List.of(product1, product2));

        List<Product> products = repository.findAll();
        Product product3 = repository.findRegularProduct("콜라").orElse(null);

        assertThat(products).containsExactlyInAnyOrder(product1, product2, product3);
    }

    @DisplayName("프로모션 제품을 이름으로 조회한다.")
    @Test
    void 프로모션_제품_조회_테스트() {
        Product promotionalProduct = new Product("콜라", 1000, 10, "탄산2+1");
        ProductRepository repository = new ProductRepository(List.of(promotionalProduct));

        Optional<Product> foundProduct = repository.findPromotionalProduct("콜라");

        assertThat(foundProduct).isPresent().get().isEqualTo(promotionalProduct);
    }

    @DisplayName("일반 제품을 이름으로 조회한다.")
    @Test
    void 일반_제품_조회_테스트() {
        Product regularProduct = new Product("사이다", 800, 15, "");
        ProductRepository repository = new ProductRepository(List.of(regularProduct));

        Optional<Product> foundProduct = repository.findRegularProduct("사이다");

        assertThat(foundProduct).isPresent().get().isEqualTo(regularProduct);
    }

    @DisplayName("프로모션 및 일반 제품을 이름으로 조회한다.")
    @Test
    void 전체_제품_조회_테스트() {
        Product promotionalProduct = new Product("콜라", 1000, 10, "탄산2+1");
        Product regularProduct = new Product("사이다", 800, 15, "");
        ProductRepository repository = new ProductRepository(List.of(promotionalProduct, regularProduct));

        Optional<Product> foundPromotional = repository.findProduct("콜라");
        Optional<Product> foundRegular = repository.findProduct("사이다");

        assertThat(foundPromotional).isPresent().get().isEqualTo(promotionalProduct);
        assertThat(foundRegular).isPresent().get().isEqualTo(regularProduct);
    }
}
