package store.dto;

public record PromotionDto(
        String name,
        int buy,
        int get,
        String startDate,
        String endDate
) {
}
