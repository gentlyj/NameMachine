package com.ifading.namemachine.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * name数据封装类 Created by yangjingsheng on 17/10/14.
 */
@Entity
public class NameBean {

    @Id
    long id;

    int mode;

    String book;

    long firstIndex;

    long secondIndex;

    String name;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public long getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(long firstIndex) {
        this.firstIndex = firstIndex;
    }

    public long getSecondIndex() {
        return secondIndex;
    }

    public void setSecondIndex(long secondIndex) {
        this.secondIndex = secondIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
