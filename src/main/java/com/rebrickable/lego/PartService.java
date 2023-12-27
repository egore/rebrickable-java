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
import com.rebrickable.lego.model.Part;
import com.rebrickable.lego.model.PartColor;
import com.rebrickable.lego.model.PartColor2;
import com.rebrickable.lego.model.Set;
import com.rebrickable.lego.responses.PartColorResponse;
import com.rebrickable.lego.responses.PartResponse;
import com.rebrickable.lego.responses.SetResponse;

import java.io.IOException;
import java.util.List;

public class PartService extends AbstractService {

    protected PartService(String apiKey, ObjectMapper mapper, String baseUrl) {
        super(apiKey, mapper, baseUrl);
    }

    public List<Part> all() throws IOException {
        return getAllInPages("/lego/parts/", PartResponse.class);
    }

    public List<Part> page(int page, int pageSize) throws IOException {
        return getPage("/lego/parts/", PartResponse.class, page, pageSize);
    }

    public Part get(String partNum) throws IOException {
        return getSingle("/lego/parts/{part_num}/".replace("{part_num}", partNum), Part.class);
    }

    public List<PartColor> colors(String partNum) throws IOException {
        return getAllInPages("/lego/parts/{part_num}/colors/".replace("{part_num}", partNum), PartColorResponse.class);
    }

    public PartColor2 color(String partNum, int colorId) throws IOException {
        return getSingle("/lego/parts/{part_num}/colors/{color_id}/".replace("{part_num}", partNum).replace("{color_id}", Integer.toString(colorId)), PartColor2.class);
    }

    public List<Set> colorSet(String partNum, int colorId) throws IOException {
        return getAllInPages("/lego/parts/{part_num}/colors/{color_id}/sets/".replace("{part_num}", partNum).replace("{color_id}", Integer.toString(colorId)), SetResponse.class);
    }
}
