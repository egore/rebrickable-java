package com.rebrickable.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebrickable.users.model.LostPart;
import com.rebrickable.users.responses.LostPartsResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LostPartsService extends AbstractUserService {

    protected LostPartsService(String apiKey, ObjectMapper mapper, String baseUrl, String userToken) {
        super(apiKey, mapper, baseUrl, userToken);
    }

    public List<LostPart> all() throws IOException {
        return getAllInPages("/users/{user_token}/lost_parts/".replace("{user_token}", userToken), LostPartsResponse.class);
    }

    /**
     * @param page page to load (starts at 1)
     * @param pageSize number of entries per page (pass 0 to use default)
     */
    public List<LostPart> page(int page, int pageSize) throws IOException {
        return getPage("/users/{user_token}/lost_parts/".replace("{user_token}", userToken), LostPartsResponse.class, page, pageSize);
    }

    public LostPart add(int partId, int quantity) throws IOException {
        return post("/users/{user_token}/lost_parts/".replace("{user_token}", userToken), LostPart.class, Map.of(
                "lost_quantity", quantity,
                "inv_part_id", partId
        ));
    }

    public void delete(int id) throws IOException {
        delete("/users/{user_token}/lost_parts/{id}/".replace("{user_token}", userToken).replace("{id}", Integer.toString(id)));
    }

}
