package com.cami7ord.viaticando.data;

public class Category {

    private int categoryId;
    private String name;
    private int accountingNumber;

    public Category() {

    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountingNumber() {
        return accountingNumber;
    }

    public void setAccountingNumber(int accountingNumber) {
        this.accountingNumber = accountingNumber;
    }

    @Override
    public String toString() {
        return name;
    }
}
