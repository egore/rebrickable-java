package com.rebrickable.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebrickable.users.model.Partlist;
import com.rebrickable.users.responses.PartlistsResponse;

import java.io.IOException;
import java.util.List;

public class PartlistsService extends AbstractUserService {

    protected PartlistsService(String apiKey, ObjectMapper mapper, String baseUrl, String userToken) {
        super(apiKey, mapper, baseUrl, userToken);
    }

    public List<Partlist> all() throws IOException {
        return getPaged("/users/{user_token}/partlists/".replace("{user_token}", userToken), PartlistsResponse.class);
    }

    public Partlist get(int listId) throws IOException {
        return getSingle("/users/{user_token}/partlists/{list_id}/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(listId)), Partlist.class);
    }

    public List<Partlist> parts(int listId) throws IOException {
        return getPaged("/users/{user_token}/partlists/{list_id}/parts/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(listId)), PartlistsResponse.class);
    }

}
