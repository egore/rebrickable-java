package com.rebrickable.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebrickable.AbstractService;
import com.rebrickable.users.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class UsersService extends AbstractService  {

    private static final Logger LOG = LoggerFactory.getLogger(UsersService.class);

    private String userToken;

    public UsersService(String apiKey, ObjectMapper mapper, String baseUrl, String username, String password) throws IOException {
        super(apiKey, mapper, baseUrl);
        login(username, password);
    }

    private void login(String username, String password) throws IOException {
        Token token = post("/users/_token/", Token.class, Map.of("username", username, "password", password));
        if (token != null) {
            this.userToken = token.userToken;
            LOG.debug("Login successful");
        } else {
            LOG.warn("Login failed");
        }
    }

    public MinifigureService minifigure() {
        return new MinifigureService(apiKey, mapper, baseUrl, userToken);
    }

    public PartlistsService partlists() {
        return new PartlistsService(apiKey, mapper, baseUrl, userToken);
    }

}
