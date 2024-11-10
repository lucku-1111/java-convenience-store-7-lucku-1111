package store.repository;

import camp.nextstep.edu.missionutils.DateTimes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionRepositoryTest {
    @DisplayName("프로모션 이름으로 조회하면 프로모션을 반환한다.")
    @Test
    void 프로모션_조회_테스트() {
        Promotion promotion1 = new Promotion("탄산2+1", 2, 1,
                DateTimes.now().toLocalDate(), DateTimes.now().toLocalDate());
        Promotion promotion2 = new Promotion("MD추천상품", 1, 1,
                DateTimes.now().toLocalDate(), DateTimes.now().toLocalDate());
        PromotionRepository repository = new PromotionRepository(List.of(promotion1, promotion2));

        Optional<Promotion> foundPromotion = repository.find("탄산2+1");

        assertThat(foundPromotion).isPresent();
        assertThat(foundPromotion.get().name()).isEqualTo("탄산2+1");
    }

    @DisplayName("존재하지 않는 프로모션을 조회하면 null을 반환한다.")
    @Test
    void 존재하지_않는_프로모션_조회_테스트() {
        Promotion promotion1 = new Promotion("탄산2+1", 2, 1,
                DateTimes.now().toLocalDate(), DateTimes.now().toLocalDate());
        Promotion promotion2 = new Promotion("MD추천상품", 1, 1,
                DateTimes.now().toLocalDate(), DateTimes.now().toLocalDate());
        PromotionRepository repository = new PromotionRepository(List.of(promotion1, promotion2));

        Optional<Promotion> foundPromotion = repository.find("석우추천상품!");

        assertThat(foundPromotion).isNotPresent();
    }
}
