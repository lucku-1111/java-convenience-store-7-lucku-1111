package store.config;

import java.io.IOException;
import store.controller.StoreController;
import store.util.LocalTimeGenerator;
import store.util.RealLocalTimeGenerator;
import store.domain.StoreManager;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.StoreService;
import store.util.ResourceFileReader;
import store.view.InputView;
import store.view.OutputView;

public class ApplicationConfig {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";

    private final ResourceFileReader resourceFileReader;

    public ApplicationConfig() throws IOException {
        this.resourceFileReader = new ResourceFileReader(PRODUCT_FILE_PATH, PROMOTION_FILE_PATH);
    }

    public StoreController storeController() throws IOException {
        return new StoreController(inputView(), outputView(), storeService());
    }

    public StoreService storeService() throws IOException {
        return new StoreService(storeManager(), realLocalTimeGenerator());
    }

    public InputView inputView() {
        return new InputView();
    }

    public OutputView outputView() {
        return new OutputView();
    }

    public StoreManager storeManager() throws IOException {
        return new StoreManager(productRepository(), promotionRepository());
    }

    public ProductRepository productRepository() throws IOException {
        return new ProductRepository(resourceFileReader.readProducts());
    }

    public PromotionRepository promotionRepository() throws IOException {
        return new PromotionRepository(resourceFileReader.readPromotions());
    }

    public LocalTimeGenerator realLocalTimeGenerator() {
        return new RealLocalTimeGenerator();
    }
}
