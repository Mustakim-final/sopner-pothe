package com.example.ourproject.Model;

public class Notice {
    private String notice,total,due,new_id,id;

    public Notice() {
    }

    public Notice(String total, String due, String new_id, String id) {
        this.total = total;
        this.due = due;
        this.new_id = new_id;
        this.id = id;
    }


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getNew_id() {
        return new_id;
    }

    public void setNew_id(String new_id) {
        this.new_id = new_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
