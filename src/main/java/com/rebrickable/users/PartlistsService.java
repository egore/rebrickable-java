package com.rebrickable.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebrickable.users.model.Partlist;
import com.rebrickable.users.responses.PartlistsResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Partlist create(Partlist partlist) throws IOException {
        Map<String, String> data = toMap(partlist);
        return post("/users/{user_token}/partlists/".replace("{user_token}", userToken), Partlist.class, data);
    }

    public void delete(int listId) throws IOException {
        delete("/users/{user_token}/partlists/{list_id}/parts/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(listId)));
    }

    public Partlist update(Partlist partlist) throws IOException {
        Map<String, String> data = toMap(partlist);
        return put("/users/{user_token}/partlists/{list_id}/parts/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(partlist.id)), Partlist.class, data);
    }

    public Partlist replace(Partlist partlist) throws IOException {
        Map<String, String> data = toMap(partlist);
        return put("/users/{user_token}/partlists/{list_id}/parts/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(partlist.id)), Partlist.class, data);
    }

    private static Map<String, String> toMap(Partlist partlist) {
        Map<String, String> data = new HashMap<>();
        data.put("is_buildable", Boolean.toString(partlist.buildable));
        data.put("name", partlist.name);
        data.put("num_parts", Integer.toString(partlist.numParts));
        return data;
    }

}
