package com.ifading.namemachine.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * name数据封装类 Created by yangjingsheng on 17/10/14.
 */
@Entity
public class NameBean {
    /**
     * 主键
     */
    @Id
    long id;

    /**
     * 模式
     */
    int mode;
    /**
     * 哪本书
     */
    String book;
    /**
     * 三字名中中间字的 index
     */
    long firstIndex;
    /**
     * 三字名中最后字的 index
     */
    long secondIndex;
    /**
     * name
     */
    String name;
    /**
     * 姓氏本的 id, 区分是哪个本的
     */

    int nameNoteId;

    @Override
    public String toString() {
        return "NameBean{" +
                "id=" + id +
                ", mode=" + mode +
                ", book='" + book + '\'' +
                ", firstIndex=" + firstIndex +
                ", secondIndex=" + secondIndex +
                ", name='" + name + '\'' +
                ", nameNoteId=" + nameNoteId +
                '}';
    }

    public int getNameNoteId() {
        return nameNoteId;
    }

    public void setNameNoteId(int nameNoteId) {
        this.nameNoteId = nameNoteId;
    }




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
