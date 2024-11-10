package store.controller;

import java.util.List;
import store.constant.OrderStatus;
import store.dto.OrderNotice;
import store.dto.PurchaseInfo;
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
        checkOrders();
    }

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

    private void checkOrders() {
        while (storeService.hasNextOrder()) {
            OrderNotice orderNotice = storeService.checkOrder();
            OrderStatus orderStatus = orderNotice.orderStatus();

            proceedOrder(orderNotice, orderStatus);
        }
    }

    private void proceedOrder(OrderNotice orderNotice, OrderStatus orderStatus) {
        if (orderStatus != OrderStatus.NOT_APPLICABLE) {
            try {
                askAndApplyUserDecision(orderNotice, orderStatus);
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
                askAndApplyUserDecision(orderNotice, orderStatus);
            }
        }
    }

    private void askAndApplyUserDecision(OrderNotice orderNotice, OrderStatus orderStatus) {
        if (orderStatus == OrderStatus.PROMOTION_AVAILABLE_ADDITIONAL_PRODUCT) {
            if (inputView.askAddFreeProduct(orderNotice)) {
                storeService.modifyOrder(orderNotice.quantity());
            }
        }
        if (orderStatus == OrderStatus.PROMOTION_STOCK_INSUFFICIENT) {
            if (!inputView.askPurchaseWithoutPromotion(orderNotice)) {
                storeService.modifyOrder(-orderNotice.quantity());
            }
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
