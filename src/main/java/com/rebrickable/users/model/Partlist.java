package com.rebrickable.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Partlist {
    public int id;
    @JsonProperty("is_buildable")
    public boolean buildable;
    public String name;
    @JsonProperty("num_parts")
    public int numParts;
}
