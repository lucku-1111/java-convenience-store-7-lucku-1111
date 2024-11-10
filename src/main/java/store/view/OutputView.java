package store.view;

import java.util.List;
import java.util.Objects;
import store.dto.ProductDto;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_FORMAT = "- %s %s원 %d개 %s";
    private static final String EMPTY_PRODUCT_FORMAT = "- %s %s원 재고 없음 %s";
    private static final String AMOUNT_FORMAT = "%,d";
    private static final String EMPTY_STRING = "";

    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printProductInventory(List<ProductDto> products) {
        ProductDto previousProduct = null;
        for (ProductDto product : products) {
            if (previousProduct != null && !previousProduct.promotion().isEmpty() &&
                    !Objects.equals(previousProduct.name(), product.name())) {
                System.out.println(formatEmptyRegularProduct(previousProduct).trim());
            }
            System.out.println(formatProduct(product).trim());
            previousProduct = product;
        }
    }

    private String formatEmptyRegularProduct(ProductDto product) {
        return String.format(EMPTY_PRODUCT_FORMAT, product.name(),
                formatAmount(product.price()), EMPTY_STRING);
    }

    private String formatProduct(ProductDto product) {
        if (product.quantity() == 0) {
            return String.format(EMPTY_PRODUCT_FORMAT, product.name(),
                    formatAmount(product.price()), product.promotion());
        }
        return String.format(PRODUCT_FORMAT, product.name(),
                formatAmount(product.price()), product.quantity(), product.promotion());
    }

    private String formatAmount(int amount) {
        return String.format(AMOUNT_FORMAT, amount);
    }
}
