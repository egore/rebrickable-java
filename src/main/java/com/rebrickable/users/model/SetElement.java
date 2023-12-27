package com.rebrickable.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rebrickable.lego.model.Set;

public class SetElement {
    @JsonProperty("list_id")
    public int id;
    public int quantity;
    @JsonProperty("include_spares")
    public boolean includeSpares;
    public Set set;
}
