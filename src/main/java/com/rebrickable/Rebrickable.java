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
package com.rebrickable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rebrickable.lego.LegoService;
import com.rebrickable.users.UsersService;
import com.rebrickable.util.SimpleCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Java implementation of the Rebrickable V3 API.
 * {@see https://rebrickable.com/api/}
 */
public class Rebrickable {

    private static final Logger LOG = LoggerFactory.getLogger(Rebrickable.class);

    private final String apiKey;
    private final String baseUrl;
    private final SimpleCache<UsersService> userServiceCache = new SimpleCache<>();

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    /**
     * @param apiKey your private API key (see Profile Settings on rebrickable.com)
     */
    public Rebrickable(String apiKey) {
        this(apiKey, "https://rebrickable.com/api/v3");
    }

    /**
     * @param apiKey your private API key (see Profile Settings on rebrickable.com)
     * @param baseUrl alternate base URL without a trailing slash (default is "https://rebrickable.com/api/v3")
     */
    public Rebrickable(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
        LOG.debug("Create rebrickable instance for URL {}", this.baseUrl);
    }

    public LegoService lego() {
        return new LegoService(apiKey, mapper, baseUrl);
    }

    public UsersService users(String username, String password) throws IOException {
        String key = username + password;
        UsersService usersService = userServiceCache.get(key);
        if (usersService == null) {
            usersService = new UsersService(apiKey, mapper, baseUrl, username, password);
            userServiceCache.put(key, usersService);
        }
        return usersService;
    }

}
