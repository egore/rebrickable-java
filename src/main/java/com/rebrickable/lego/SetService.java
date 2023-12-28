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
import com.rebrickable.lego.model.Set;
import com.rebrickable.lego.model.SetMinifigure;
import com.rebrickable.lego.model.SetPart;
import com.rebrickable.lego.responses.SetMinifigureResponse;
import com.rebrickable.lego.responses.SetPartsResponse;
import com.rebrickable.lego.responses.SetResponse;

import java.io.IOException;
import java.util.List;

public class SetService extends AbstractService {

    protected SetService(String apiKey, ObjectMapper mapper, String baseUrl) {
        super(apiKey, mapper, baseUrl);
    }

    public List<Set> all() throws IOException {
        return getAllInPages("/lego/sets/", SetResponse.class);
    }

    /**
     * @param page page to load (starts at 1)
     * @param pageSize number of entries per page (pass 0 to use default)
     */
    public List<Set> page(int page, int pageSize) throws IOException {
        return getPage("/lego/sets/", SetResponse.class, page, pageSize);
    }

    public Set get(String setNum) throws IOException {
        return getSingle("/lego/sets/{set_num}/".replace("{set_num}", fixup(setNum)), Set.class);
    }

    public List<Set> alternates(String setNum) throws IOException {
        return getAllInPages("/lego/sets/{set_num}/alternates/".replace("{set_num}", fixup(setNum)), SetResponse.class);
    }

    public List<SetMinifigure> minifigures(String setNum) throws IOException {
        return getAllInPages("/lego/sets/{set_num}/minifigs/".replace("{set_num}", fixup(setNum)), SetMinifigureResponse.class);
    }

    public List<SetPart> parts(String setNum) throws IOException {
        return getAllInPages("/lego/sets/{set_num}/parts/".replace("{set_num}", fixup(setNum)), SetPartsResponse.class);
    }

    public List<Set> sets(String setNum) throws IOException {
        return getAllInPages("/lego/sets/{set_num}/sets/".replace("{set_num}", fixup(setNum)), SetResponse.class);
    }

    /**
     * Ensure sets are postfixed by '-1' if they don't have a postfix.
     */
    private String fixup(String setNum) {
        if (setNum == null) {
            return null;
        }
        if (setNum.indexOf('-') < 0) {
            return setNum + "-1";
        }
        return setNum;
    }

}
