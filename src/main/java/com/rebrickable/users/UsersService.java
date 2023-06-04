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
