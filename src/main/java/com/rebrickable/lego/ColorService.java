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
import com.rebrickable.lego.model.Color;
import com.rebrickable.lego.responses.ColorResponse;

import java.io.IOException;
import java.util.List;

public class ColorService extends AbstractService {

    protected ColorService(String apiKey, ObjectMapper mapper, String baseUrl) {
        super(apiKey, mapper, baseUrl);
    }

    public List<Color> all() throws IOException {
        return getAllInPages("/lego/colors/", ColorResponse.class);
    }

    /**
     * @param page page to load (starts at 1)
     * @param pageSize number of entries per page (pass 0 to use default)
     */
    public List<Color> page(int page, int pageSize) throws IOException {
        return getPage("/lego/colors/", ColorResponse.class, page, pageSize);
    }

    /**
     * Load a specific color by its ID.
     * @param colorId the ID of the color (e.g. "-1" for unknown color, or "0" for black)
     * @return the color found by the given ID
     */
    public Color get(int colorId) throws IOException{
        return getSingle("/lego/colors/{id}/".replace("{id}", Integer.toString(colorId)), Color.class);
    }

}
