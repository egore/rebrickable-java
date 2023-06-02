package com.rebrickable.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebrickable.AbstractService;

public class AbstractUserService extends AbstractService {

    protected final String userToken;

    protected AbstractUserService(String apiKey, ObjectMapper mapper, String baseUrl, String userToken) {
        super(apiKey, mapper, baseUrl);
        this.userToken = userToken;
    }

}
