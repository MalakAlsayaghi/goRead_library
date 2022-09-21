package com.goread.library.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    String orderId, userId, libraryId, deliveryId, locationId, img_url, description, orderDate, paymentMethod;
    int totalPrice, deliveryPrice;
    boolean isAccepted, isRejected,isLibraryAccepted;
    ArrayList<MyCart> items;

    public Order(String orderId, String userId, String libraryId, String deliveryId, String locationId,
                 String img_url, String description, String orderDate, String paymentMethod, int totalPrice,
                 int deliveryPrice, boolean isAccepted, boolean isRejected, boolean isLibraryAccepted, ArrayList<MyCart> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.libraryId = libraryId;
        this.deliveryId = deliveryId;
        this.locationId = locationId;
        this.img_url = img_url;
        this.description = description;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
        this.isAccepted = isAccepted;
        this.isRejected = isRejected;
        this.isLibraryAccepted = isLibraryAccepted;
        this.items = items;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public boolean isLibraryAccepted() {
        return isLibraryAccepted;
    }

    public void setLibraryAccepted(boolean libraryAccepted) {
        isLibraryAccepted = libraryAccepted;
    }

    public Order() {

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public ArrayList<MyCart> getItems() {
        return items;
    }

    public void setItems(ArrayList<MyCart> items) {
        this.items = items;
    }
}