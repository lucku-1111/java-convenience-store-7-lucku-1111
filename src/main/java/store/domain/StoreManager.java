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
}
