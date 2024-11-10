package store.dto;

import store.constant.OrderStatus;

public record OrderNotice(
        OrderStatus orderStatus,
        String productName,
        int quantity
) {
}
