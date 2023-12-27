package com.rebrickable.users.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetCreateResponse {
    @JsonProperty("set_num")
    public String setNum;
    public int quantity;
    @JsonProperty("include_spares")
    public boolean includeSpares;
}
