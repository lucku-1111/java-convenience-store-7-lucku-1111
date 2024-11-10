package store;

import java.io.IOException;
import store.config.ApplicationConfig;
import store.controller.StoreController;

public class Application {
    public static void main(String[] args) {
        try {
            ApplicationConfig config = new ApplicationConfig();
            StoreController storeController = config.storeController();

            storeController.run();
        } catch (IOException e) {
            System.out.println("resources 파일 오류 발생: " + e.getMessage());
        }
    }
}
