package store.dto;

public record ProductReceiptDto(
        String name,
        int quantity,
        int price
) {
}
