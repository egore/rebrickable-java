package com.rebrickable.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    @JsonProperty("user_token")
    public String userToken;
}
