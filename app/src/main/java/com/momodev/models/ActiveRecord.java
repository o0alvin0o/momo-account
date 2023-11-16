package com.momodev.models;

import java.io.Serial;
import java.io.Serializable;

public abstract class ActiveRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
