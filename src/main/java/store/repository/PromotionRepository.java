package store.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Promotion;

public class PromotionRepository {
    private final List<Promotion> promotions;

    public PromotionRepository(List<Promotion> promotions) {
        this.promotions = new ArrayList<>(promotions);
    }

    public Optional<Promotion> find(String name) {
        return promotions.stream().filter(promotion -> promotion.name().equals(name)).findFirst();
    }
}
