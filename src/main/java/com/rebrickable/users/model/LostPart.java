package com.rebrickable.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rebrickable.lego.model.SetPart;

public class LostPart {
    @JsonProperty("lost_part_id")
    public int id;
    @JsonProperty("lost_quantity")
    public int quantity;
    @JsonProperty("inv_part")
    public SetPart part;
}
