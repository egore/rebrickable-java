package com.rebrickable.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebrickable.AbstractService;
import com.rebrickable.exceptions.RebrickableException;
import com.rebrickable.users.MinifigureService;
import com.rebrickable.lego.exceptions.*;
import com.rebrickable.users.exceptions.InvalidCredentialsException;
import com.rebrickable.users.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UsersService {

    private static final Logger LOG = LoggerFactory.getLogger(UsersService.class);

    private final String apiKey;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private String userToken;

    public UsersService(String apiKey, ObjectMapper mapper, String baseUrl, String username, String password) throws IOException {
        this.apiKey = apiKey;
        this.mapper = mapper;
        this.baseUrl = baseUrl;
        login(username, password);
    }

    private void login(String username, String password) throws IOException {
        URL url = new URL(baseUrl + "/users/_token/");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Authorization", "key " + apiKey);
        connection.addRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (var writer = new DataOutputStream(connection.getOutputStream())) {
            writer.writeBytes("username=");
            writer.writeBytes(URLEncoder.encode(username, StandardCharsets.UTF_8));
            writer.writeBytes("&password=");
            writer.writeBytes(URLEncoder.encode(password, StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            try (InputStream inputStream = connection.getInputStream()) {
                Token token = mapper.readValue(inputStream, Token.class);
                this.userToken = token.userToken;
                LOG.debug("Login successful");
            }
        } else if (responseCode == 400) {
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
        } else {
            LOG.warn("Unexpected status code {} for {}", responseCode, url);
        }
    }

    public MinifigureService minifigure() {
        return new MinifigureService(apiKey, mapper, baseUrl, userToken);
    }

}
