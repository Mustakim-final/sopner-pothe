package com.example.ourproject.Model;

public class EditorNotice {
    String notice,new_id,id;

    public EditorNotice() {
    }

    public EditorNotice(String notice, String new_id, String id) {
        this.notice = notice;
        this.new_id = new_id;
        this.id = id;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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
