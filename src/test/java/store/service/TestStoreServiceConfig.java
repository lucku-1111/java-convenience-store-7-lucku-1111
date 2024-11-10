package store.service;

import store.domain.StoreManager;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.util.ResourceFileReader;

import java.io.IOException;

public class TestStoreServiceConfig {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";

    public StoreService storeService() throws IOException {
        return new StoreService(storeManager(), testLocalTimeGenerator());
    }

    public StoreManager storeManager() throws IOException {
        return new StoreManager(productRepository(), promotionRepository());
    }

    public ProductRepository productRepository() throws IOException {
        return new ProductRepository(resourceFileReader().readProducts());
    }

    public PromotionRepository promotionRepository() throws IOException {
        return new PromotionRepository(resourceFileReader().readPromotions());
    }

    public TestLocalDateGenerator testLocalTimeGenerator() {
        return new TestLocalDateGenerator();
    }

    private ResourceFileReader resourceFileReader() {
        return new ResourceFileReader(PRODUCT_FILE_PATH, PROMOTION_FILE_PATH);
    }
}
