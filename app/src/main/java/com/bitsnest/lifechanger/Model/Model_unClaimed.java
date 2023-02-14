package com.bitsnest.lifechanger.Model;

public class Model_unClaimed {
    private String id,amount,date,totalbalance;

    public Model_unClaimed(String id, String amount, String date, String totalbalance) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.totalbalance = totalbalance;
    }

    public String getTotalbalance() {
        return totalbalance;
    }

    public void setTotalbalance(String totalbalance) {
        this.totalbalance = totalbalance;
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
}
