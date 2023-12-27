/*
 * Copyright © 2023 Christoph Brill
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
        return getAllInPages("/users/{user_token}/partlists/".replace("{user_token}", userToken), PartlistsResponse.class);
    }

    public Partlist get(int listId) throws IOException {
        return getSingle("/users/{user_token}/partlists/{list_id}/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(listId)), Partlist.class);
    }

    public List<Partlist> parts(int listId) throws IOException {
        return getAllInPages("/users/{user_token}/partlists/{list_id}/parts/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(listId)), PartlistsResponse.class);
    }

    public Partlist create(Partlist partlist) throws IOException {
        var data = toMap(partlist);
        return post("/users/{user_token}/partlists/".replace("{user_token}", userToken), Partlist.class, data);
    }

    public void delete(int listId) throws IOException {
        delete("/users/{user_token}/partlists/{list_id}/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(listId)));
    }

    public Partlist update(Partlist partlist) throws IOException {
        var data = toMap(partlist);
        return patch("/users/{user_token}/partlists/{list_id}/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(partlist.id)), Partlist.class, data);
    }

    public Partlist replace(Partlist partlist) throws IOException {
        var data = toMap(partlist);
        return put("/users/{user_token}/partlists/{list_id}/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(partlist.id)), Partlist.class, data);
    }

    private static Map<String, Object> toMap(Partlist partlist) {
        Map<String, Object> data = new HashMap<>();
        data.put("is_buildable", partlist.buildable);
        data.put("name", partlist.name);
        data.put("num_parts", partlist.numParts);
        return data;
    }

}
