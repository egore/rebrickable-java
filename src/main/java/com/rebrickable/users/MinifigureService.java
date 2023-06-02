package com.rebrickable.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebrickable.users.model.Minifigure;
import com.rebrickable.users.responses.MinifigureResponse;

import java.io.IOException;
import java.util.List;

public class MinifigureService extends AbstractUserService {

    protected MinifigureService(String apiKey, ObjectMapper mapper, String baseUrl, String userToken) {
        super(apiKey, mapper, baseUrl, userToken);
    }

    public List<Minifigure> all() throws IOException {
        return getPaged("/users/{user_token}/minifigs/".replace("{user_token}", userToken), MinifigureResponse.class);
    }

}
