package store.service;

import java.util.List;
import store.domain.StoreManager;
import store.dto.ProductDto;

public class StoreService {
    private final StoreManager storeManager;
    private final LocalTimeGenerator timeGenerator;
    private final List<Order> orders;

    public StoreService(StoreManager storeManager) {
        this.storeManager = storeManager;
        this.timeGenerator = timeGenerator;
        this.orders = new ArrayList<>();
    }

    public List<ProductDto> getProducts() {
        return storeManager.getProductDtos();
    }

    public void resetOrders() {
        orders.clear();
    }

    public void applyPurchaseInfo(List<PurchaseInfo> purchases) {
        for (PurchaseInfo purchaseInfo : purchases) {
            storeManager.validatePurchaseInfo(purchaseInfo);
            orders.add(new Order(
                    purchaseInfo.name(),
                    purchaseInfo.quantity(),
                    timeGenerator.today()
            ));
        }
        orderIterator = orders.iterator();
    }
    }
}
