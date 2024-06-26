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
package com.rebrickable.integration.lego;

import com.rebrickable.Rebrickable;
import com.rebrickable.lego.ColorService;
import com.rebrickable.lego.model.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ColorServiceIT extends AbstractLegoServiceIT {

    private static ColorService SERVICE;

    @BeforeAll
    public static void initService() {
        SERVICE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY"))
                .lego()
                .color();
    }

    @Test
    public void testAll() throws IOException {
        assertThat(SERVICE.all()).isNotNull();
    }

    @Test
    public void testAllPage() throws IOException {
        List<Color> colors = SERVICE.all(1, 1);
        Assertions.assertNotNull(colors);
        Assertions.assertEquals(1, colors.size());
        System.err.println(colors.get(0).name);
    }

    @Test
    public void testGet() throws IOException {
        assertThat(SERVICE.get(1)).isNotNull();
    }

}
