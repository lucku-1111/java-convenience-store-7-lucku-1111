package store.dto;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private List<ProductReceipt> products;
    private List<ProductReceipt> freeProducts;
    private ProductReceipt totalOriginInfo;
    private int totalFreePrice;
    private int membershipPrice;
    private int finalPayment;

    public Receipt() {
        this.products = new ArrayList<>();
        this.freeProducts = new ArrayList<>();
        this.totalFreePrice = 0;
        this.membershipPrice = 0;
        this.finalPayment = 0;
    }

    public List<ProductReceipt> getProducts() {
        return products;
    }

    public List<ProductReceipt> getFreeProducts() {
        return freeProducts;
    }

    public ProductReceipt getTotalOriginInfo() {
        return totalOriginInfo;
    }

    public int getTotalFreePrice() {
        return totalFreePrice;
    }

    public int getMembershipPrice() {
        return membershipPrice;
    }

    public int getFinalPayment() {
        return finalPayment;
    }

    public void setProducts(List<ProductReceipt> products) {
        this.products = products;
    }

    public void setFreeProducts(List<ProductReceipt> freeProducts) {
        this.freeProducts = freeProducts;
    }

    public void setTotalOriginInfo(ProductReceipt totalOriginInfo) {
        this.totalOriginInfo = totalOriginInfo;
    }

    public void setTotalFreePrice(int totalFreePrice) {
        this.totalFreePrice = totalFreePrice;
    }

    public void setMembershipPrice(int membershipPrice) {
        this.membershipPrice = membershipPrice;
    }

    public void setFinalPayment(int finalPayment) {
        this.finalPayment = finalPayment;
    }
}
