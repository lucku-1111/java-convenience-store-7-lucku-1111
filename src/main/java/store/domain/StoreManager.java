package store.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.dto.ProductDto;
import store.dto.PurchaseInfo;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

import static store.constant.ErrorMessages.NOT_EXIST_PRODUCT_MESSAGE;
import static store.constant.ErrorMessages.QUANTITY_EXCEED_MESSAGE;

public class StoreManager {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public StoreManager(ProductRepository productRepository,
                        PromotionRepository promotionRepository
    ) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    public List<ProductDto> getProductDtos() {
        List<ProductDto> productDtos = new ArrayList<>();
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            productDtos.add(convertToProductDto(product));
        }
        return productDtos;
    }

    public void validatePurchaseInfo(PurchaseInfo purchaseInfo) {
        Optional<Product> promotionalProduct = productRepository.findPromotionalProduct(purchaseInfo.name());
        Optional<Product> regularProduct = productRepository.findRegularProduct(purchaseInfo.name());

        validateProductExist(promotionalProduct.orElse(null), regularProduct.orElse(null));
        validateProductQuantity(
                promotionalProduct.map(Product::getQuantity).orElse(0),
                regularProduct.map(Product::getQuantity).orElse(0),
                purchaseInfo.quantity()
        );
    }

    public Integer isValidForAdditionalProduct(Order order) {
        Product product = productRepository.findProduct(order.getName()).orElse(null);
        Promotion promotion = promotionRepository.find(product.getPromotion()).orElse(null);
        if (!canApplyPromotion(product, promotion, order)) {
            return 0;
        }
        return getAdditionalProductQuantity(
                product.getQuantity(),
                promotion,
                order
        );
    }

    public Integer isStockInsufficient(Order order) {
        Product product = productRepository.findProduct(order.getName()).orElse(null);
        Promotion promotion = promotionRepository.find(product.getPromotion()).orElse(null);
        if (!canApplyPromotion(product, promotion, order)) {
            return 0;
        }
        int promotionalQuantity = product.getQuantity();
        return order.getQuantity() -
                (promotionalQuantity - (promotionalQuantity % (promotion.buy() + promotion.get())));
    }

    public List<Integer> calculateOrder(Order order) {
        Product product = productRepository.findProduct(order.getName()).orElseThrow();
        List<Integer> result = new ArrayList<>();

        calculatePromotionalAndFreeProduct(product, order, result);
        result.add(calculateRegularProduct(order));
        result.add(product.getPrice());
        return result;
    }

    private ProductDto convertToProductDto(Product product) {
        return new ProductDto(
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getPromotion()
        );
    }

    private void validateProductExist(Product promotionalProduct, Product regularProduct) {
        if (promotionalProduct == null && regularProduct == null) {
            throw new IllegalArgumentException(NOT_EXIST_PRODUCT_MESSAGE);
        }
    }

    private void validateProductQuantity(int promotionalQuantity, int regularQuantity, int purchaseQuantity) {
        if (promotionalQuantity + regularQuantity < purchaseQuantity) {
            throw new IllegalArgumentException(QUANTITY_EXCEED_MESSAGE);
        }
    }

    private boolean canApplyPromotion(Product promotionalProduct, Promotion promotion, Order order) {
        if (promotionalProduct == null || promotion == null) {
            return false;
        }
        return !order.getCreationDate().isBefore(promotion.startDate()) &&
                !order.getCreationDate().isAfter(promotion.endDate());
    }

    private Integer getAdditionalProductQuantity(int promotionalQuantity, Promotion promotion, Order order) {
        if (promotionalQuantity >= order.getQuantity() + promotion.get() &&
                order.getQuantity() % (promotion.buy() + promotion.get()) == promotion.buy()) {
            return promotion.get();
        }
        return 0;
    }

    private void calculatePromotionalAndFreeProduct(Product product, Order order, List<Integer> result) {
        Promotion promotion = promotionRepository.find(product.getPromotion()).orElse(null);

        if (canApplyPromotion(product, promotion, order)) {
            result.add(comparePromotionalProductAndOrder(product, promotion, order));
            result.add(result.getFirst() / (promotion.buy() + promotion.get()));
            return;
        }
        result.add(0);
        result.add(0);
    }

    private Integer calculateRegularProduct(Order order) {
        Product product = productRepository.findProduct(order.getName()).orElse(null);
        if (order.getQuantity() == 0 || product == null) {
            return 0;
        }
        if (product.getPromotion().isEmpty()) {
            return compareRegularProductAndOrder(product, order);
        }
        Product regularProduct = productRepository.findRegularProduct(order.getName()).orElse(null);
        return compareRegularProductAndOrder(product, order) +
                compareRegularProductAndOrder(regularProduct, order);
    }

    private Integer comparePromotionalProductAndOrder(Product product, Promotion promotion, Order order) {
        if (product.getQuantity() >= order.getQuantity()) {
            return updateProductAndOrder(product, order,
                    order.getQuantity() - (order.getQuantity() % (promotion.buy() + promotion.get())));
        }
        return updateProductAndOrder(product, order,
                product.getQuantity() - (product.getQuantity() % (promotion.buy() + promotion.get())));
    }

    private Integer compareRegularProductAndOrder(Product product, Order order) {
        if (product.getQuantity() >= order.getQuantity()) {
            return updateProductAndOrder(product, order, order.getQuantity());
        }
        return updateProductAndOrder(product, order, product.getQuantity());
    }

    private Integer updateProductAndOrder(Product product, Order order, int quantityDelta) {
        product.soldQuantity(quantityDelta);
        order.updateQuantity(-quantityDelta);
        return quantityDelta;
    }
}
