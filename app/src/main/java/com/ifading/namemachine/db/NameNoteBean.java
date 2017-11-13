package com.ifading.namemachine.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by yangjingsheng on 17/10/20.
 */
@Entity
public class NameNoteBean {
    @Id
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    private String itemName;

    @Override
    public String toString() {
        return "NameNoteBean{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
