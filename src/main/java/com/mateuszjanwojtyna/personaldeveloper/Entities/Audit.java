package com.mateuszjanwojtyna.personaldeveloper.Entities;

import com.mateuszjanwojtyna.personaldeveloper.Enums.BussinessHook;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Audit {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;//in future maybe connect ti User entity...

    @Column
    private BussinessHook type;

    @Column
    private String clas;

    @Column
    private String method;

    @Column
    private Timestamp timestamp;

    public Audit() {
    }

    public Audit(String username, BussinessHook type, String clas, String method, Timestamp timestamp) {
        this.username = username;
        this.type = type;
        this.clas = clas;
        this.method = method;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BussinessHook getType() {
        return type;
    }

    public void setType(BussinessHook type) {
        this.type = type;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
