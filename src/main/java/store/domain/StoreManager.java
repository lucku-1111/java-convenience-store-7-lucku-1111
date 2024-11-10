package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.dto.ProductDto;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

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

    private ProductDto convertToProductDto(Product product) {
        return new ProductDto(
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getPromotion()
        );
    }
}
