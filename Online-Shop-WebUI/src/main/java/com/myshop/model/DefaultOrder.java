package com.myshop.model;

import java.util.Arrays;

public class DefaultOrder implements Order {
    private int customerId;
    private String creditCardNumber;
    private Product[] products;

    @Override
    public boolean isCreditCardNumberValid(String userInput) {
        return userInput != null && userInput.matches("\\d{16}");
    }

    @Override
    public void setCreditCardNumber(String userInput) {
            this.creditCardNumber = userInput;
    }

    @Override
    public void setProducts(Product[] products) {
        this.products = products;
    }

    @Override
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public int getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "DefaultOrder{" +
                "customerId=" + customerId +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", products=" + Arrays.toString(products) +
                '}';
    }
}
