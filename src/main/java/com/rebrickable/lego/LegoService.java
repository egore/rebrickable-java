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
package com.rebrickable.lego;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebrickable.AbstractService;

/**
 * A wrapper service for all data independent of the user.
 *
 * The data provided and consumed by these services contains global information like colors, minifigures or sets.
 * To manage parts, sets or similar owned by a user please refer to the {@link com.rebrickable.users.UsersService}.
 */
public class LegoService extends AbstractService {

    public LegoService(String apiKey, ObjectMapper mapper, String baseUrl) {
        super(apiKey, mapper, baseUrl);
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
