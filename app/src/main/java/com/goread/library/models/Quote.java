package com.goread.library.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Quote {
    String id, bookName, content, date;


    public Quote() {
    }

    public Quote(String id, String bookName, String content) {
        this.id = id;
        this.bookName = bookName;
        this.content = content;
        this.date = new SimpleDateFormat("dd/MM/yyy HH:mm").format(Calendar.getInstance().getTime());

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
