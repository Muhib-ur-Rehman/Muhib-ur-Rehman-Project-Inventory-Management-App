package com.example.InventoryManager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;
    private String orderNum;
    private String name;
    private int itemId;
    private int qty;
    private double price;
    private String orderStatus;
    private String paymentStatus;
    private String accountNum;

    public OrderInfo() {
    }

    public OrderInfo(int orderId, String orderNum, String name, int itemId, int qty, double price, String orderStatus, String paymentStatus, String accountNum) {
        this.orderId = orderId;
        this.orderNum = orderNum;
        this.name = name;
        this.itemId = itemId;
        this.qty = qty;
        this.price = price;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.accountNum = accountNum;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderId=" + orderId +
                ", orderNum='" + orderNum + '\'' +
                ", name='" + name + '\'' +
                ", itemId=" + itemId +
                ", qty=" + qty +
                ", price=" + price +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", accountNum='" + accountNum + '\'' +
                '}';
    }
}
