package store.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Product;

public class ProductRepository {
    private static final String EMPTY_STRING = "";

    private final List<Product> products;

    public ProductRepository(List<Product> products) {
        this.products = new ArrayList<>(products);
        setUp();
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
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

    public Optional<Product> findProduct(String name) {
        return findPromotionalProduct(name).or(() -> findRegularProduct(name));
    }

    private void setUp() {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            if (!product.getPromotion().isEmpty() && findRegularProduct(product.getName()).isEmpty()) {
                products.add(++i, new Product(product.getName(), product.getPrice(), 0, EMPTY_STRING));
            }
        }
    }
}
