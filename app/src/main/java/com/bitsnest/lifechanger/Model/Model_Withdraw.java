package com.bitsnest.lifechanger.Model;

public class Model_Withdraw {
    private String id,amount,date,ac_withdraw;

    public Model_Withdraw(String id, String amount, String date, String ac_withdraw) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.ac_withdraw = ac_withdraw;
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

    public String getAc_withdraw() {
        return ac_withdraw;
    }

    public void setAc_withdraw(String ac_withdraw) {
        this.ac_withdraw = ac_withdraw;
    }
}
