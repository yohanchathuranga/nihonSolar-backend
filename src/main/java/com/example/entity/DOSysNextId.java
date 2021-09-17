package com.example.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sys_next_id")
public class DOSysNextId implements Serializable {

    @Id
    private String id;
    private String type;
    private int nextId;
    private String prefix;
    private String status;

    public DOSysNextId() {
    }

    public DOSysNextId(String id, String type, int nextId, String prefix, String status) {
        this.id = id;
        this.type = type;
        this.nextId = nextId;
        this.prefix = prefix;
        this.status = status;
    }

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

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
