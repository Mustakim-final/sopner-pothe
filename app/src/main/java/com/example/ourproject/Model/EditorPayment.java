package com.example.ourproject.Model;

public class EditorPayment {
    private String  new_id,name,amount,date,status,id;

    public EditorPayment() {
    }

    public EditorPayment(String new_id, String name, String amount, String date, String status,String id) {
        this.new_id = new_id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.id=id;
    }

    public String getNew_id() {
        return new_id;
    }

    public void setNew_id(String new_id) {
        this.new_id = new_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
