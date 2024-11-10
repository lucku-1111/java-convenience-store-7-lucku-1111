package store.controller;

import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final StoreService storeService;

    public StoreController(InputView inputView, OutputView outputView, StoreService storeService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.storeService = storeService;
    }

    public void run() {
        do {
            runStore();
        } while (wantMorePurchase());
    }

    private void runStore() {
        outputView.printWelcomeMessage();
        outputView.printProductInventory(storeService.getProducts());
        enterOrderInfo();

    private void enterOrderInfo() {
        try {
            storeService.resetOrders();
            List<PurchaseInfo> purchases = inputView.readPurchases();
            storeService.applyPurchaseInfo(purchases);
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            enterOrderInfo();
        }
    }
    }

    private boolean wantMorePurchase() {
        try {
            return inputView.askForAdditionalPurchase();
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            return wantMorePurchase();
        }
    }
}
