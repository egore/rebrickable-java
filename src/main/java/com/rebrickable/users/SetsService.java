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
import com.rebrickable.lego.model.Set;
import com.rebrickable.users.model.SetElement;
import com.rebrickable.users.responses.SetCreateResponse;
import com.rebrickable.users.responses.SetUpdateResponse;
import com.rebrickable.users.responses.SetsResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetsService extends AbstractUserService {

    protected SetsService(String apiKey, ObjectMapper mapper, String baseUrl, String userToken) {
        super(apiKey, mapper, baseUrl, userToken);
    }

    public List<SetElement> all() throws IOException {
        return getAllInPages("/users/{user_token}/sets/".replace("{user_token}", userToken), SetsResponse.class);
    }

    public SetCreateResponse create(SetElement set) throws IOException {
        var data = toMap(set);
        return post("/users/{user_token}/sets/".replace("{user_token}", userToken), SetCreateResponse.class, data);
    }

    public void delete(String setNum) throws IOException {
        super.delete("/users/{user_token}/sets/{set_num}/".replace("{user_token}", userToken).replace("{set_num}", setNum));
    }

    public SetElement get(String setNum) throws IOException {
        return getSingle("/users/{user_token}/sets/{set_num}/".replace("{user_token}", userToken).replace("{set_num}", setNum), SetElement.class);
    }

    public SetUpdateResponse update(SetElement set) throws IOException {
        var data = toMap(set);
        return put("/users/{user_token}/sets/{set_num}/".replace("{user_token}", userToken).replace("{set_num}", set.set.setNum), SetUpdateResponse.class, data);
    }

    // TODO /api/v3/users/{user_token}/sets/sync/

    private static Map<String, Object> toMap(SetElement set) {
        Map<String, Object> data = new HashMap<>();
        data.put("set_num", set.set.setNum);
        data.put("quantity", set.quantity);
        data.put("include_spares", set.includeSpares);
        return data;
    }

}
