package store.dto;

public record ProductDto(
        String name,
        int price,
        int quantity,
        String promotion
) {
}
