package store.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import store.constant.OrderStatus;
import store.util.LocalDateGenerator;
import store.domain.Order;
import store.domain.ReceiptConverter;
import store.domain.StoreManager;
import store.dto.OrderNotice;
import store.domain.OrderResult;
import store.dto.ProductDto;
import store.dto.PurchaseInfo;
import store.dto.Receipt;

public class StoreService {
    private final StoreManager storeManager;
    private final LocalDateGenerator timeGenerator;
    private final List<Order> orders;
    private Iterator<Order> orderIterator;
    private Order currentOrder;

    public StoreService(StoreManager storeManager, LocalDateGenerator timeGenerator) {
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

    public void modifyOrder(int quantityDelta) {
        currentOrder.updateQuantity(quantityDelta);
    }

    public Receipt calculateOrders(boolean isMembershipDiscount) {
        List<OrderResult> orderResults = new ArrayList<>();
        ReceiptConverter converter = new ReceiptConverter();

        for (Order order : orders) {
            List<Integer> result = storeManager.calculateOrder(order);
            orderResults.add(new OrderResult(order.getName(), result.getFirst(),
                    result.get(1), result.get(2), result.getLast()));
        }
        return converter.convertToReceipt(orderResults, isMembershipDiscount);
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
