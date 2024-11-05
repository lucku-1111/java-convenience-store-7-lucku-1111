package store.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import store.dto.ProductDto;
import store.dto.PromotionDto;

public class ResourceFileReader {
    private static final Path PRODUCT_FILE_PATH = Paths.get("src/main/resources/products.md");
    private static final Path PROMOTION_FILE_PATH = Paths.get("src/main/resources/promotions.md");
    private static final int HEADER_LINE = 1;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;
    private static final int BUY_INDEX = 1;
    private static final int GET_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;
    private static final String DELIMITER = ",";

    public List<ProductDto> readProducts() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE_PATH.toFile()))) {
            return reader.lines()
                    .skip(HEADER_LINE)
                    .map(this::parseProduct)
                    .toList();
        }
    }

    public List<PromotionDto> readPromotions() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(PROMOTION_FILE_PATH.toFile()))) {
            return reader.lines()
                    .skip(HEADER_LINE)
                    .map(this::parsePromotion)
                    .toList();
        }
    }

    private ProductDto parseProduct(String line) {
        List<String> data = Arrays.stream(line.split(DELIMITER)).toList();
        String name = data.getFirst();
        int price = Integer.parseInt(data.get(PRICE_INDEX));
        int quantity = Integer.parseInt(data.get(QUANTITY_INDEX));
        String promotion = data.get(PROMOTION_INDEX);

        return new ProductDto(name, price, quantity, promotion);
    }

    private PromotionDto parsePromotion(String line) {
        List<String> data = Arrays.stream(line.split(DELIMITER)).toList();
        String name = data.getFirst();
        int buy = Integer.parseInt(data.get(BUY_INDEX));
        int get = Integer.parseInt(data.get(GET_INDEX));
        String startDate = data.get(START_DATE_INDEX);
        String endDate = data.get(END_DATE_INDEX);

        return new PromotionDto(name, buy, get, startDate, endDate);
    }
}
