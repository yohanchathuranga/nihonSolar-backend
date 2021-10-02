package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "drop_down_items")
public class DODropDownItem implements Serializable {

    @Id
    private String id;

    @Column(name = "type")
    private String type;

    @Column(name = "data")
    private String data;

    @Column(name = "deleted")
    private boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }



    public DODropDownItem(String id, String data) {
        this.id = id;
        this.data = data;
    }

    public DODropDownItem(String id) {
        this.id = id;
    }

    public DODropDownItem() {
    }

    public DODropDownItem(String id, String type, String data, boolean deleted) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.deleted = deleted;
    }

    public static interface DODropDownListItem {

        String getId();

        String getData();
    }

}
