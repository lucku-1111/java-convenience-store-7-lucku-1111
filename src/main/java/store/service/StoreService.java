package store.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import store.constant.OrderStatus;
import store.domain.LocalTimeGenerator;
import store.domain.Order;
import store.domain.StoreManager;
import store.dto.OrderNotice;
import store.domain.OrderResult;
import store.dto.ProductDto;
import store.dto.PurchaseInfo;

public class StoreService {
    private final StoreManager storeManager;
    private final LocalTimeGenerator timeGenerator;
    private final List<Order> orders;
    private Iterator<Order> orderIterator;
    private Order currentOrder;

    public StoreService(StoreManager storeManager, LocalTimeGenerator timeGenerator) {
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

    public boolean hasNextOrder() {
        return orderIterator.hasNext();
    }

    public OrderNotice checkOrder() {
        currentOrder = orderIterator.next();
        return determineOrderNotice(currentOrder);
    }

    private OrderNotice determineOrderNotice(Order order) {
        if (storeManager.isValidForAdditionalProduct(order) != 0) {
            return new OrderNotice(OrderStatus.PROMOTION_AVAILABLE_ADDITIONAL_PRODUCT, order.getName(),
                    storeManager.isValidForAdditionalProduct(order));
        }
        if (storeManager.isStockInsufficient(order) > 0) {
            return new OrderNotice(OrderStatus.PROMOTION_STOCK_INSUFFICIENT, order.getName(),
                    storeManager.isStockInsufficient(order));
        }
        return new OrderNotice(OrderStatus.NOT_APPLICABLE, order.getName(),
                storeManager.isStockInsufficient(order));
    }
    }
}
