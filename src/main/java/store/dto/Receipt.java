package store.dto;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private List<ProductReceiptDto> products;
    private List<ProductReceiptDto> freeProducts;
    private ProductReceiptDto totalOriginInfo;
    private int totalFreePrice;
    private int membershipPrice;
    private int totalPayment;

    public Receipt() {
        this.products = new ArrayList<>();
        this.freeProducts = new ArrayList<>();
        this.totalFreePrice = 0;
        this.membershipPrice = 0;
        this.totalPayment = 0;
    }

    public List<ProductReceiptDto> getProducts() {
        return products;
    }

    public List<ProductReceiptDto> getFreeProducts() {
        return freeProducts;
    }

    public ProductReceiptDto getTotalOriginInfo() {
        return totalOriginInfo;
    }

    public int getTotalFreePrice() {
        return totalFreePrice;
    }

    public int getMembershipPrice() {
        return membershipPrice;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setProducts(List<ProductReceiptDto> products) {
        this.products = products;
    }

    public void setFreeProducts(List<ProductReceiptDto> freeProducts) {
        this.freeProducts = freeProducts;
    }

    public void setTotalOriginInfo(ProductReceiptDto totalOriginInfo) {
        this.totalOriginInfo = totalOriginInfo;
    }

    public void setTotalFreePrice(int totalFreePrice) {
        this.totalFreePrice = totalFreePrice;
    }

    public void setMembershipPrice(int membershipPrice) {
        this.membershipPrice = membershipPrice;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }
}
