package store.domain;

public record OrderResult(
        String productName,
        int promotionalQuantity,
        int freeQuantity,
        int regularQuantity,
        int price
) {
}
