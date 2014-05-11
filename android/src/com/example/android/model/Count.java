package com.example.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by gabriel on 5/11/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class Count {
    @JsonProperty("mobId")
    private int id;

    @JsonProperty("count")
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
