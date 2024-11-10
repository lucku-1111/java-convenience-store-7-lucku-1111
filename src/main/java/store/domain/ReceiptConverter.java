package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.dto.ProductReceiptDto;
import store.dto.Receipt;

public class ReceiptConverter {
    public Receipt convertToReceipt(List<OrderResult> orderResults, boolean isMembershipDiscount) {
        Receipt receipt = new Receipt();
        receipt.setProducts(createProductList(orderResults));
        receipt.setFreeProducts(createFreeProductList(orderResults));
        receipt.setTotalOriginInfo(calculateTotalOriginInfo(orderResults));
        receipt.setTotalFreePrice(calculateTotalFreePrice(orderResults));
        receipt.setMembershipPrice(calculateMembershipDiscount(orderResults, isMembershipDiscount));
        receipt.setTotalPayment(receipt.getTotalOriginInfo().price() -
                receipt.getTotalFreePrice() - receipt.getMembershipPrice());
        return receipt;
    }

    private List<ProductReceiptDto> createProductList(List<OrderResult> orderResults) {
        List<ProductReceiptDto> products = new ArrayList<>();
        for (OrderResult orderResult : orderResults) {
            int quantity = orderResult.promotionalQuantity() + orderResult.regularQuantity();
            int price = quantity * orderResult.price();
            products.add(new ProductReceiptDto(orderResult.productName(), quantity, price));
        }
        return products;
    }

    private List<ProductReceiptDto> createFreeProductList(List<OrderResult> orderResults) {
        List<ProductReceiptDto> freeProducts = new ArrayList<>();
        for (OrderResult orderResult : orderResults) {
            if (orderResult.freeQuantity() > 0) {
                freeProducts.add(new ProductReceiptDto(orderResult.productName(), orderResult.freeQuantity(), 0));
            }
        }
        return freeProducts;
    }

    private ProductReceiptDto calculateTotalOriginInfo(List<OrderResult> orderResults) {
        int totalQuantity = orderResults.stream()
                .mapToInt(order -> order.promotionalQuantity() + order.regularQuantity())
                .sum();
        int totalPrice = orderResults.stream()
                .mapToInt(order -> (order.promotionalQuantity() + order.regularQuantity()) * order.price())
                .sum();
        return new ProductReceiptDto(null, totalQuantity, totalPrice);
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
        return Math.min(totalRegularPrice * 30 / 100, 8000);
    }
}
