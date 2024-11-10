package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.dto.PurchaseInfo;

import static store.constant.ErrorMessages.INVALID_FORMAT_MESSAGE;

public class InputView {
    private static final String PURCHASE_PROMPT = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
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
}
