package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.dto.ProductReceipt;
import store.dto.Receipt;

public class ReceiptConverter {
    private static final int DISCOUNT_RATE = 30;
    private static final int PERCENT_DIVISOR = 100;
    private static final int MAX_DISCOUNT_AMOUNT = 8000;

    public Receipt convertToReceipt(List<OrderResult> orderResults, boolean isMembershipDiscount) {
        Receipt receipt = new Receipt();

        receipt.setProducts(createProductList(orderResults));
        receipt.setFreeProducts(createFreeProductList(orderResults));
        receipt.setTotalOriginInfo(calculateTotalOriginInfo(orderResults));
        receipt.setTotalFreePrice(calculateTotalFreePrice(orderResults));
        receipt.setMembershipPrice(calculateMembershipDiscount(orderResults, isMembershipDiscount));
        receipt.setFinalPayment(receipt.getTotalOriginInfo().price() -
                receipt.getTotalFreePrice() - receipt.getMembershipPrice());
        return receipt;
    }

    private List<ProductReceipt> createProductList(List<OrderResult> orderResults) {
        List<ProductReceipt> products = new ArrayList<>();

        for (OrderResult orderResult : orderResults) {
            int quantity = orderResult.promotionalQuantity() + orderResult.regularQuantity();
            int price = quantity * orderResult.price();
            products.add(new ProductReceipt(orderResult.productName(), quantity, price));
        }
        return products;
    }

    private List<ProductReceipt> createFreeProductList(List<OrderResult> orderResults) {
        List<ProductReceipt> freeProducts = new ArrayList<>();

        for (OrderResult orderResult : orderResults) {
            if (orderResult.freeQuantity() > 0) {
                freeProducts.add(new ProductReceipt(orderResult.productName(),
                        orderResult.freeQuantity(), 0));
            }
        }
        return freeProducts;
    }

    private ProductReceipt calculateTotalOriginInfo(List<OrderResult> orderResults) {
        int totalQuantity = orderResults.stream()
                .mapToInt(order -> order.promotionalQuantity() + order.regularQuantity())
                .sum();
        int totalPrice = orderResults.stream()
                .mapToInt(order -> (order.promotionalQuantity() + order.regularQuantity()) * order.price())
                .sum();

        return new ProductReceipt(null, totalQuantity, totalPrice);
    }

    private int calculateTotalFreePrice(List<OrderResult> orderResults) {
        return orderResults.stream()
                .mapToInt(order -> order.freeQuantity() * order.price())
                .sum();
    }

    private int calculateMembershipDiscount(List<OrderResult> orderResults, boolean isMembershipDiscount) {
        if (!isMembershipDiscount) {
            return 0;
        }
        int totalRegularPrice = orderResults.stream()
                .mapToInt(order -> order.regularQuantity() * order.price())
                .sum();

        return Math.min(totalRegularPrice * DISCOUNT_RATE / PERCENT_DIVISOR, MAX_DISCOUNT_AMOUNT);
    }
}
