package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.dto.OrderNotice;
import store.dto.PurchaseInfo;

import static store.constant.ErrorMessages.INVALID_FORMAT_MESSAGE;
import static store.constant.ErrorMessages.INVALID_INPUT_MESSAGE;

public class InputView {
    private static final String PURCHASE_PROMPT = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String ADDITIONAL_PURCHASE_PROMPT = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String PROMOTION_AVAILABLE_PROMPT =
            "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String PROMOTION_STOCK_INSUFFICIENT_PROMPT =
            "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String MEMBERSHIP_DISCOUNT_PROMPT = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String POSITIVE_ANSWER = "Y";
    private static final String NEGATIVE_ANSWER = "N";
    private static final String ORDER_DELIMITER = ",";
    private static final String NAME_QUANTITY_DELIMITER = "-";
    private static final String EMPTY_STRING = "";
    private static final String OPEN_BRACKET = "[";
    private static final String CLOSE_BRACKET = "]";
    private static final String BRACKET_REMOVE_REGEX = "^\\[|]$";
    private static final int EXPECTED_ORDER_PARTS_SIZE = 2;

    public List<PurchaseInfo> readPurchases() {
        printPrompt(PURCHASE_PROMPT);
        return parsePurchases(Console.readLine());
    }

    public boolean askAddFreeProduct(OrderNotice orderNotice) {
        printPrompt(String.format(
                PROMOTION_AVAILABLE_PROMPT,
                orderNotice.productName(),
                orderNotice.quantity()
        ));
        return getUserConfirmation();
    }

    public boolean askPurchaseWithoutPromotion(OrderNotice orderNotice) {
        printPrompt(String.format(
                PROMOTION_STOCK_INSUFFICIENT_PROMPT,
                orderNotice.productName(),
                orderNotice.quantity()
        ));
        return getUserConfirmation();
    }

    public boolean askForMembershipDiscount() {
        printPrompt(MEMBERSHIP_DISCOUNT_PROMPT);
        return getUserConfirmation();
    }

    public boolean askForAdditionalPurchase() {
        printPrompt(ADDITIONAL_PURCHASE_PROMPT);
        return getUserConfirmation();
    }

    private void printPrompt(String prompt) {
        System.out.println(prompt);
    }

    private List<PurchaseInfo> parsePurchases(String input) {
        List<String> orderEntries = splitInputByDelimiter(input);
        List<PurchaseInfo> purchases = new ArrayList<>();

        for (String orderEntry : orderEntries) {
            List<String> parsedOrder = parseOrderEntry(orderEntry);
            purchases.add(new PurchaseInfo(
                    parsedOrder.getFirst(),
                    Integer.parseInt(parsedOrder.getLast())
            ));
        }
        return purchases;
    }

    private List<String> splitInputByDelimiter(String input) {
        return Arrays.stream(input.split(ORDER_DELIMITER)).toList();
    }

    private List<String> parseOrderEntry(String orderEntry) {
        validateBrackets(orderEntry);
        String sanitizedEntry = removeBrackets(orderEntry);
        List<String> parsedOrder = Arrays.stream(sanitizedEntry.split(NAME_QUANTITY_DELIMITER)).toList();
        validateParsedOrder(parsedOrder);
        return parsedOrder;
    }

    private void validateBrackets(String orderEntry) {
        if (!orderEntry.startsWith(OPEN_BRACKET) || !orderEntry.endsWith(CLOSE_BRACKET)) {
            throw new IllegalArgumentException(INVALID_FORMAT_MESSAGE);
        }
    }

    private String removeBrackets(String orderEntry) {
        return orderEntry.replaceAll(BRACKET_REMOVE_REGEX, EMPTY_STRING);
    }

    private void validateParsedOrder(List<String> parsedOrder) {
        validateOrderPartsSize(parsedOrder);
        validateQuantityIsInteger(parsedOrder.getLast());
    }

    private void validateOrderPartsSize(List<String> parsedOrder) {
        if (parsedOrder.size() != EXPECTED_ORDER_PARTS_SIZE) {
            throw new IllegalArgumentException(INVALID_FORMAT_MESSAGE);
        }
    }

    private void validateQuantityIsInteger(String quantity) {
        try {
            Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_FORMAT_MESSAGE);
        }
    }

    private boolean getUserConfirmation() {
        String input = Console.readLine();

        if (input.equals(POSITIVE_ANSWER)) {
            return true;
        }
        if (input.equals(NEGATIVE_ANSWER)) {
            return false;
        }
        throw new IllegalArgumentException(INVALID_INPUT_MESSAGE);
    }
}
