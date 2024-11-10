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

    public List<Promotion> findAll() {
        return new ArrayList<>(promotions);
    }

    public void add(Promotion promotion) {
        promotions.add(promotion);
    }

    public Optional<Promotion> find(String name) {
        return promotions.stream().filter(p -> p.name().equals(name)).findFirst();
    }
}
