
package com.example.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SerializedMob {
    @JsonProperty("id")
    private Object id;
    @JsonProperty("mobs")
    private Mob[] mobs;


    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Mob[] getMobs() {
        return mobs;
    }

    public void setMobs(Mob[] most) {
        this.mobs = most;
    }
}

