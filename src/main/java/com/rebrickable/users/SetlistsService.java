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
import com.rebrickable.users.model.Setlist;
import com.rebrickable.users.responses.SetlistsResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetlistsService extends AbstractUserService {

    protected SetlistsService(String apiKey, ObjectMapper mapper, String baseUrl, String userToken) {
        super(apiKey, mapper, baseUrl, userToken);
    }

    public List<Setlist> all() throws IOException {
        return getAllInPages("/users/{user_token}/setlists/".replace("{user_token}", userToken), SetlistsResponse.class);
    }

    public Setlist create(Setlist setlist) throws IOException {
        var data = toMap(setlist);
        return post("/users/{user_token}/setlists/".replace("{user_token}", userToken), Setlist.class, data);
    }

    public void delete(int listId) throws IOException {
        delete("/users/{user_token}/setlists/{list_id}/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(listId)));
    }

    public Setlist get(int listId) throws IOException {
        return getSingle("/users/{user_token}/setlists/{list_id}/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(listId)), Setlist.class);
    }

    public Setlist update(Setlist setlist) throws IOException {
        var data = toMap(setlist);
        return patch("/users/{user_token}/setlists/{list_id}/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(setlist.id)), Setlist.class, data);
    }

    public Setlist replace(Setlist setlist) throws IOException {
        var data = toMap(setlist);
        return put("/users/{user_token}/setlists/{list_id}/".replace("{user_token}", userToken).replace("{list_id}", Integer.toString(setlist.id)), Setlist.class, data);
    }

    private static Map<String, Object> toMap(Setlist setlist) {
        Map<String, Object> data = new HashMap<>();
        data.put("is_buildable", setlist.buildable);
        data.put("name", setlist.name);
        data.put("num_sets", setlist.numSets);
        return data;
    }

}
