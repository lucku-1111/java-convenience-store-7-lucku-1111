package store.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import store.domain.Product;
import store.domain.Promotion;

public class ResourceFileReader {
    private static final int HEADER_LINE = 1;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;
    private static final int BUY_INDEX = 1;
    private static final int GET_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;
    private static final String DELIMITER = ",";
    private static final String NULL_STRING = "null";
    private static final String EMPTY_STRING = "";

    private final Path productFilePath;
    private final Path promotionFilePath;

    public ResourceFileReader(String productFilePath, String promotionFilePath) {
        this.productFilePath = Paths.get(productFilePath);
        this.promotionFilePath = Paths.get(promotionFilePath);
    }

    public List<Product> readProducts() throws IOException {
        return readFile(productFilePath, this::parseProduct);
    }

    public List<Promotion> readPromotions() throws IOException {
        return readFile(promotionFilePath, this::parsePromotion);
    }

    private <T> List<T> readFile(Path filePath, Function<String, T> parser) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            return reader.lines()
                    .skip(HEADER_LINE)
                    .map(parser)
                    .toList();
        }
    }

    private Product parseProduct(String line) {
        List<String> data = splitLine(line);
        String name = data.getFirst();
        int price = Integer.parseInt(data.get(PRICE_INDEX));
        int quantity = Integer.parseInt(data.get(QUANTITY_INDEX));
        String promotion = getPromotion(data.get(PROMOTION_INDEX));
        return new Product(name, price, quantity, promotion);
    }

    private Promotion parsePromotion(String line) {
        List<String> data = splitLine(line);
        String name = data.getFirst();
        int buy = Integer.parseInt(data.get(BUY_INDEX));
        int get = Integer.parseInt(data.get(GET_INDEX));
        LocalDate startDate = LocalDate.parse(data.get(START_DATE_INDEX));
        LocalDate endDate = LocalDate.parse(data.get(END_DATE_INDEX));
        return new Promotion(name, buy, get, startDate, endDate);
    }

    private List<String> splitLine(String line) {
        return Arrays.stream(line.split(DELIMITER)).toList();
    }

    private String getPromotion(String promotion) {
        if (promotion.equals(NULL_STRING)) {
            return EMPTY_STRING;
        }
        return promotion;
    }
}
