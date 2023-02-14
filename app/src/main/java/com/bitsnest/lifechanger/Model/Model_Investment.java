package com.bitsnest.lifechanger.Model;

public class Model_Investment {
    private String id,amount,date,ac_deposit,receipt;

    public Model_Investment(String id, String amount, String date, String ac_deposit, String receipt) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.ac_deposit = ac_deposit;
        this.receipt = receipt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAc_deposit() {
        return ac_deposit;
    }

    public void setAc_deposit(String ac_deposit) {
        this.ac_deposit = ac_deposit;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
}
