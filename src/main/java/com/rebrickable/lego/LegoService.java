package com.rebrickable.lego;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LegoService {

    private final String apiKey;
    private final ObjectMapper mapper;
    private final String baseUrl;

    public LegoService(String apiKey, ObjectMapper mapper, String baseUrl) {
        this.apiKey = apiKey;
        this.mapper = mapper;
        this.baseUrl = baseUrl;
    }

    public ColorService color() {
        return new ColorService(apiKey, mapper, baseUrl);
    }

    public ElementService element() {
        return new ElementService(apiKey, mapper, baseUrl);
    }

    public MinifigureService minifigure() {
        return new MinifigureService(apiKey, mapper, baseUrl);
    }

    public PartCategoryService partCategory() {
        return new PartCategoryService(apiKey, mapper, baseUrl);
    }

    public SetService set() {
        return new SetService(apiKey, mapper, baseUrl);
    }

    public ThemeService theme() {
        return new ThemeService(apiKey, mapper, baseUrl);
    }

    public PartService part() {
        return new PartService(apiKey, mapper, baseUrl);
    }

}
