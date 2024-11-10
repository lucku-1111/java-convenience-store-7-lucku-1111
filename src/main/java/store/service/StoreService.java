package store.service;

import java.util.List;
import store.domain.StoreManager;
import store.dto.ProductDto;

public class StoreService {
    private final StoreManager storeManager;

    public StoreService(StoreManager storeManager) {
        this.storeManager = storeManager;
    }

    public List<ProductDto> getProducts() {
        return storeManager.getProductDtos();
    }
}
