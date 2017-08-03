package com.r2.scau.moblieofficing.bean;

import java.util.Date;

/**
 * Created by 嘉进 on 10:33.
 */

public class Notice {
    private String title;
    private String author;
    private Date time;
    private String Content;

    public Notice() {
    }

    public Notice(String title, String author, Date time, String content) {
        this.title = title;
        this.author = author;
        this.time = time;
        Content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
