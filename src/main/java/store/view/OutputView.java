package store.view;

import java.util.List;
import store.dto.ProductDto;
import store.dto.ProductReceiptDto;
import store.dto.Receipt;

public class OutputView {
    private static final String ERROR_PREFIX = "[ERROR] ";
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_FORMAT = "- %s %s원 %d개 %s";
    private static final String EMPTY_PRODUCT_FORMAT = "- %s %s원 재고 없음 %s";
    private static final String AMOUNT_FORMAT = "%,d";

    private static final String STORE_HEADER = "==============W 편의점================";
    private static final String PROMOTION_SECTION = "=============증     정===============";
    private static final String TOTAL_SECTION = "====================================";
    private static final String PRODUCT_NAME_HEADER = "상품명";
    private static final String QUANTITY_HEADER = "수량";
    private static final String PRICE_HEADER = "금액";
    private static final String COLUMN_TITLES = "%-16s%-10s%s";
    private static final String PURCHASED_PRODUCT_FORMAT = "%-16s%-10d%-10s";
    private static final String FREE_PRODUCT_FORMAT = "%-16s%-10d";
    private static final String TOTAL_PRICE_FORMAT = "%-16s%-10d%-10s";
    private static final String DISCOUNT_PRICE_FORMAT = "%-26s-%-10s";
    private static final String FINAL_PAYMENT_FORMAT = "%-27s%-10s";

    private static final String TOTAL_PURCHASE_LABEL = "총구매액";
    private static final String PROMOTION_DISCOUNT_LABEL = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT_LABEL = "멤버십할인";
    private static final String FINAL_PAYMENT_LABEL = "내실돈";

    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printErrorMessage(String errorMessage) {
        System.out.println(ERROR_PREFIX + errorMessage);
    }

    public void printProductInventory(List<ProductDto> products) {
        for (ProductDto product : products) {
            System.out.println(formatProduct(product).trim());
        }
    }

    public void printReceipt(Receipt receipt) {
        printHeader();
        printPurchasedProducts(receipt);
        printFreeProducts(receipt);
        printTotals(receipt);
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

    private void printHeader() {
        System.out.println(STORE_HEADER);
        System.out.println(String.format(
                COLUMN_TITLES,
                PRODUCT_NAME_HEADER,
                QUANTITY_HEADER,
                PRICE_HEADER)
                .trim()
        );
    }

    private void printPurchasedProducts(Receipt receipt) {
        for (ProductReceiptDto product : receipt.getProducts()) {
            System.out.println(String.format(
                    PURCHASED_PRODUCT_FORMAT,
                    product.name(),
                    product.quantity(),
                    formatAmount(product.price()))
                    .trim()
            );
        }
    }

    private void printFreeProducts(Receipt receipt) {
        System.out.println(PROMOTION_SECTION);
        for (ProductReceiptDto freeProduct : receipt.getFreeProducts()) {
            System.out.println(String.format(FREE_PRODUCT_FORMAT, freeProduct.name(),
                    freeProduct.quantity()).trim());
        }
    }

    private void printTotals(Receipt receipt) {
        System.out.println(TOTAL_SECTION);
        printTotalPurchase(receipt);
        printPromotionDiscount(receipt);
        printMembershipDiscount(receipt);
        printFinalPayment(receipt);
    }

    private void printTotalPurchase(Receipt receipt) {
        System.out.println(String.format(
                TOTAL_PRICE_FORMAT,
                TOTAL_PURCHASE_LABEL,
                receipt.getTotalOriginInfo().quantity(),
                formatAmount(receipt.getTotalOriginInfo().price()))
                .trim()
        );
    }

    private void printPromotionDiscount(Receipt receipt) {
        System.out.println(String.format(
                DISCOUNT_PRICE_FORMAT,
                PROMOTION_DISCOUNT_LABEL,
                formatAmount(receipt.getTotalFreePrice()))
                .trim()
        );
    }

    private void printMembershipDiscount(Receipt receipt) {
        System.out.println(String.format(
                DISCOUNT_PRICE_FORMAT,
                MEMBERSHIP_DISCOUNT_LABEL,
                formatAmount(receipt.getMembershipPrice()))
                .trim()
        );
    }

    private void printFinalPayment(Receipt receipt) {
        System.out.println(String.format(
                FINAL_PAYMENT_FORMAT,
                FINAL_PAYMENT_LABEL,
                formatAmount(receipt.getFinalPayment()))
                .trim()
        );
    }
}