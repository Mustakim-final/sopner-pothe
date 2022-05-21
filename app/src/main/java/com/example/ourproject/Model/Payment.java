package com.example.ourproject.Model;

public class Payment {
    private String name,amount,date,transaction,id,status;

    public Payment() {
    }

    public Payment(String name, String amount, String date, String transaction, String id,String status) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.transaction = transaction;
        this.id = id;
        this.status=status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
