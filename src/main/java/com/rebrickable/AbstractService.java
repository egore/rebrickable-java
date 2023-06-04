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
import com.rebrickable.exceptions.InvalidDataException;
import com.rebrickable.exceptions.RebrickableException;
import com.rebrickable.lego.exceptions.*;
import com.rebrickable.responses.PagedResponse;
import com.rebrickable.users.exceptions.InvalidCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractService.class);

    protected final String apiKey;
    protected final ObjectMapper mapper;
    protected final String baseUrl;
    private final HttpClient client;

    protected AbstractService(String apiKey, ObjectMapper mapper, String baseUrl) {
        this.apiKey = apiKey;
        this.mapper = mapper;
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
    }

    protected <T, U extends PagedResponse<T>> List<T> getPaged(String url, Class<U> responseClass) throws IOException {
        // Prepare the result
        List<T> result = null;

        // Prefix the URL with the baseUrl
        url = baseUrl + url;

        while (url != null) {

            // Prepare the request
            LOG.debug("Getting page from {}", url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "key " + apiKey)
                    .header("Accept", "application/json")
                    .build();

            // Send request and map result to JSON
            HttpResponse<Supplier<U>> response;
            try {
                response = client.send(request, responseInfo -> {
                    int statusCode = responseInfo.statusCode();
                    if (statusCode == 200) {
                        return HttpResponse.BodySubscribers.mapping(
                                HttpResponse.BodySubscribers.ofInputStream(),
                                inputStream -> () -> {
                                    try (InputStream stream = inputStream) {
                                        return mapper.readValue(stream, responseClass);
                                    } catch (IOException e) {
                                        throw new InvalidDataException(e);
                                    }
                                });
                    } else {
                        return mapError(statusCode);
                    }

                });
            } catch (IOException | InterruptedException e) {
                throw new RebrickableException(e);
            }

            U body = response.body().get();
            if (result == null) {
                result = new ArrayList<>(body.count);
            }

            result.addAll(body.results);

            // Switch to next page (null means no next page and will break the loop)
            url = body.next;

        }

        return result;
    }

    private <U> HttpResponse.BodySubscriber<Supplier<U>> mapError(int statusCode) {
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofInputStream(),
                inputStream -> () -> {
                    if (statusCode == 400) {
                        try (InputStream stream = inputStream) {
                            if (stream.available() > 0) {
                                throw new RebrickableException("An unknown error occured during your request: " + toString(stream));
                            }
                        } catch (IOException e) {
                            throw new RebrickableException(e);
                        }
                        throw new RebrickableException("An unknown error occured during your request");
                    } else if (statusCode == 401) {
                        throw new InvalidAPIKeyException("You provided an invalid API key. Please check validity at rebrickable.com");
                    } else if (statusCode == 403) {
                        try (InputStream stream = inputStream) {
                            throw new InvalidCredentialsException(mapper.readValue(stream, ExceptionResponse.class).detail);
                        } catch (IOException e) {
                            throw new RebrickableException(e);
                        }
                    } else if (statusCode == 404) {
                        throw new NotFoundException("The object you requested was not found. Please check if the provided identifier is valid");
                    } else if (statusCode == 429) {
                        try (InputStream stream = inputStream) {
                            throw new RequestThrottledException(mapper.readValue(stream, ExceptionResponse.class).detail);
                        } catch (IOException e) {
                            throw new RebrickableException(e);
                        }
                    } else if (statusCode == 502) {
                        throw new RebrickableException("The system currently seems to be overloaded. Please try again later");
                    } else {
                        LOG.warn("Unexpected status code {}", statusCode);
                        try (InputStream stream = inputStream) {
                            if (stream.available() > 0) {
                                LOG.warn(toString(stream));
                            }
                        } catch (IOException e) {
                            throw new RebrickableException(e);
                        }
                    }
                    return (U) null;
                });
    }

    private String toString(InputStream inputStream) throws IOException {
        var result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

    protected <T, U extends PagedResponse<T>> List<T> getPaged(String url, Class<U> responseClass, int page, int pageSize) {

        // Postfix the URL with necessary page and page_size parametera
        if (url.indexOf('?') < 0) {
            url += "?";
        } else {
            url += "&";
        }
        url += "page={page}&page_size={page_size}".replace("{page}", Integer.toString(page)).replace("{page_size}", Integer.toString(pageSize));

        // Prefix the URL with the baseUrl
        url = baseUrl + url;

        // Prepare the request
        LOG.debug("Getting page from {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "key " + apiKey)
                .header("Accept", "application/json")
                .build();

        // Send request and map result to JSON
        HttpResponse<Supplier<U>> response;
        try {
            response = client.send(request, responseInfo -> {
                int statusCode = responseInfo.statusCode();
                if (statusCode == 200) {
                    return HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofInputStream(),
                            inputStream -> () -> {
                                try (InputStream stream = inputStream) {
                                    return mapper.readValue(stream, responseClass);
                                } catch (IOException e) {
                                    throw new InvalidDataException(e);
                                }
                            });
                } else {
                    return mapError(statusCode);
                }

            });
        } catch (IOException | InterruptedException e) {
            throw new RebrickableException(e);
        }

        return response.body().get().results;
    }

    protected <T> T getSingle(String url, Class<T> responseClass) throws IOException {

        // Prefix the URL with the baseUrl
        url = baseUrl + url;

        // Prepare the request
        LOG.debug("Getting single from {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "key " + apiKey)
                .header("Accept", "application/json")
                .build();

        // Send request and map result to JSON
        HttpResponse<Supplier<T>> response;
        try {
            response = client.send(request, responseInfo -> {
                int statusCode = responseInfo.statusCode();
                if (statusCode == 200) {
                    return HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofInputStream(),
                            inputStream -> () -> {
                                try (InputStream stream = inputStream) {
                                    return mapper.readValue(stream, responseClass);
                                } catch (IOException e) {
                                    throw new InvalidDataException(e);
                                }
                            });
                } else {
                    return mapError(statusCode);
                }

            });
        } catch (IOException | InterruptedException e) {
            throw new RebrickableException(e);
        }

        return response.body().get();
    }

    protected <T> T post(String url, Class<T> responseClass, Map<String, Object> data) throws IOException {
        return upload("POST", url, responseClass, data);
    }

    protected <T> T patch(String url, Class<T> responseClass, Map<String, Object> data) throws IOException {
        return upload("PATCH", url, responseClass, data);
    }

    protected <T> T put(String url, Class<T> responseClass, Map<String, Object> data) throws IOException {
        return upload("PUT", url, responseClass, data);
    }

    private <T> T upload(String method, String url, Class<T> responseClass, Map<String, Object> data) throws IOException {

        // Prefix the URL with the baseUrl
        url = baseUrl + url;

        // Prepare the request
        LOG.debug("{}ing data to {}", method, url);
        var buffer = new StringBuilder();
        boolean first = true;
        for (var entry : data.entrySet()) {
            if (!first) {
                buffer.append("&");
            }
            buffer.append(entry.getKey());
            buffer.append("=");
            Object value = entry.getValue();
            if (value instanceof String) {
                buffer.append(URLEncoder.encode((String) value, StandardCharsets.UTF_8));
            } else if (value instanceof Short) {
                buffer.append(value);
            } else if (value instanceof Integer) {
                buffer.append(value);
            } else if (value instanceof Long) {
                buffer.append(value);
            } else if (value instanceof Float) {
                buffer.append(value);
            } else if (value instanceof Double) {
                buffer.append(value);
            } else if (value instanceof Boolean) {
                buffer.append(value);
            } else {
                throw new RebrickableException("Datatype " + value.getClass() + " not yet supported. Please add if appropriate");
            }
            first = false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method(method, HttpRequest.BodyPublishers.ofString(buffer.toString()))
                .header("Authorization", "key " + apiKey)
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        // Send request and map result to JSON
        HttpResponse<Supplier<T>> response;
        try {
            response = client.send(request, responseInfo -> {
                int statusCode = responseInfo.statusCode();
                if (statusCode == 200 || statusCode == 201) {
                    return HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofInputStream(),
                            inputStream -> () -> {
                                try (InputStream stream = inputStream) {
                                    return mapper.readValue(stream, responseClass);
                                } catch (IOException e) {
                                    throw new InvalidDataException(e);
                                }
                            });
                } else {
                    return mapError(statusCode);
                }

            });
        } catch (IOException | InterruptedException e) {
            throw new RebrickableException(e);
        }

        return response.body().get();
    }

    protected void delete(String url) throws IOException {

        // Prefix the URL with the baseUrl
        url = baseUrl + url;

        // Prepare the request
        LOG.debug("Deleting page from {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("DELETE", HttpRequest.BodyPublishers.noBody())
                .header("Authorization", "key " + apiKey)
                .header("Accept", "application/json")
                .build();

        // Send request and map result to JSON
        try {
            client.send(request, responseInfo -> {
                int statusCode = responseInfo.statusCode();
                if (statusCode != 204) {
                    return this.<Void>mapError(statusCode);
                } else {
                    return HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofInputStream(),
                            inputStream -> () -> (Void) null);
                }
            });
        } catch (IOException | InterruptedException e) {
            throw new RebrickableException(e);
        }
    }

}
