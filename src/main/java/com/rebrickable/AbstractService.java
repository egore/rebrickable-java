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
import com.rebrickable.exceptions.RebrickableException;
import com.rebrickable.lego.exceptions.*;
import com.rebrickable.responses.PagedResponse;
import com.rebrickable.users.exceptions.InvalidCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractService.class);

    private final String apiKey;
    private final ObjectMapper mapper;
    private final String baseUrl;

    protected AbstractService(String apiKey, ObjectMapper mapper, String baseUrl) {
        this.apiKey = apiKey;
        this.mapper = mapper;
        this.baseUrl = baseUrl;
    }

    protected <T> List<T> getPaged(String url, Class<? extends PagedResponse<T>> responseClass) throws IOException {
        // Prepare the result
        List<T> result = null;

        url = baseUrl + url;

        while (url != null) {

            // Load the data
            LOG.debug("Getting page from {}", url);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.addRequestProperty("Authorization", "key " + apiKey);
            int responseCode = connection.getResponseCode();

            url = null;

            if (responseCode == 200) {
                PagedResponse<T> response;
                try (InputStream inputStream = connection.getInputStream()) {
                    response = mapper.readValue(inputStream, responseClass);

                    if (result == null) {
                        result = new ArrayList<>(response.count);
                    }

                    result.addAll(response.results);
                }

                // Continue to the next page
                if (response.next != null) {
                    url = response.next;
                }

            } else {
                handleError(connection, responseCode);
            }

        }

        return result;
    }

    private void handleError(HttpsURLConnection connection, int responseCode) throws IOException {
        if (responseCode == 400) {
            throw new RebrickableException("An unknown error occured during your request");
        } else if (responseCode == 401) {
            throw new InvalidAPIKeyException("You provided an invalid API key. Please check validity at rebrickable.com");
        } else if (responseCode == 403) {
            try (InputStream inputStream = connection.getInputStream()) {
                var response = mapper.readValue(inputStream, ExceptionResponse.class);
                throw new InvalidCredentialsException(response.detail);
            }
        } else if (responseCode == 404) {
            throw new NotFoundException("The object you requested was not found. Please check if the provided identifier is valid");
        } else if (responseCode == 429) {
            try (InputStream inputStream = connection.getInputStream()) {
                var response = mapper.readValue(inputStream, ExceptionResponse.class);
                throw new RequestThrottledException(response.detail);
            }
        } else if (responseCode == 502) {
            throw new RebrickableException("The system currently seems to be overloaded. Please try again later");
        } else {
            LOG.warn("Unexpected status code {}", responseCode);
        }
    }

    protected <T> List<T> getPaged(String url, Class<? extends PagedResponse<T>> responseClass, int page, int pageSize) throws IOException {

        if (url.indexOf('?') < 0) {
            url += "?";
        } else {
            url += "&";
        }
        url += "page={page}&page_size={page_size}".replace("{page}", Integer.toString(page).replace("{page_size}", Integer.toString(pageSize)));

        // Load the data
        LOG.debug("Getting page from {}", url);
        HttpsURLConnection connection = (HttpsURLConnection) new URL(baseUrl + url).openConnection();
        connection.addRequestProperty("Authorization", "key " + apiKey);
        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            PagedResponse<T> response;
            try (InputStream inputStream = connection.getInputStream()) {
                response = mapper.readValue(inputStream, responseClass);

                return response.results;
            }
        } else {
            handleError(connection, responseCode);
        }

        return null;
    }

    protected <T> T getSingle(String url, Class<T> responseClass) throws IOException {
        // Load the data
        HttpsURLConnection connection = (HttpsURLConnection) new URL(baseUrl + url).openConnection();
        connection.addRequestProperty("Authorization", "key " + apiKey);
        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            try (InputStream inputStream = connection.getInputStream()) {
                return mapper.readValue(inputStream, responseClass);
            }
        } else {
            handleError(connection, responseCode);
        }

        return null;
    }

}
