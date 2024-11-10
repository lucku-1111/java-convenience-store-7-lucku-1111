package store.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Product;

public class ProductRepository {
    private final List<Product> products;

    public ProductRepository(List<Product> products) {
        this.products = new ArrayList<>(products);  // 초기 데이터를 기반으로 리스트 생성
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public void add(Product product) {
        products.add(product);
    }

    public Optional<Product> findPromotionalProduct(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name) && !product.getPromotion().isEmpty())
                .findFirst();
    }

    public Optional<Product> findRegularProduct(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name) && product.getPromotion().isEmpty())
                .findFirst();
    }

    public void updatePromotionalStock(String name, int quantitySold) {
        findPromotionalProduct(name).ifPresent(product -> product.updateQuantity(quantitySold));
    }

    public void updateRegularStock(String name, int quantitySold) {
        findRegularProduct(name).ifPresent(product -> product.updateQuantity(quantitySold));
    }

    public Optional<Product> findProduct(String name) {
        return findPromotionalProduct(name).or(() -> findRegularProduct(name));
    }
}