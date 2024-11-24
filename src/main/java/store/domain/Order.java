package store.domain;

import java.time.LocalDate;

public class Order {
    private final String name;
    private int quantity;
    private final LocalDate creationDate;

    public Order(String name, int quantity, LocalDate creationDate) {
        this.name = name;
        this.quantity = quantity;
        this.creationDate = creationDate;
    }

    public void updateQuantity(int quantityDelta) {
        quantity += quantityDelta;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
}
