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

import com.rebrickable.Rebrickable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PartServiceTest extends AbstractLegoServiceTest {

    private static PartService SERVICE;

    @BeforeAll
    public static void initService() {
        SERVICE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY"))
                .lego()
                .part();
    }

    @Test
    @Disabled("Loads to much data")
    public void testAll() throws IOException {
        assertThat(SERVICE.all()).isNotNull();
    }

    @Test
    public void testAllPage() throws IOException {
        assertThat(SERVICE.page(1, 1)).isNotNull();
    }

    @Test
    public void testGet() throws IOException {
        assertThat(SERVICE.get("3069b")).isNotNull();
        assertThat(SERVICE.get("3069a")).isNotNull();
        assertThat(SERVICE.get("973c27h27")).isNotNull();
        assertThat(SERVICE.get("3004")).isNotNull();
    }

    @Test
    public void testColors() throws IOException {
        assertThat(SERVICE.colors("973c27h27")).isNotNull();
    }

    @Test
    public void testColor() throws IOException {
        assertThat(SERVICE.color("973c27h27", 15)).isNotNull();
    }

    @Test
    public void testColorSet() throws IOException {
        assertThat(SERVICE.colorSet("973c27h27", 15)).isNotNull();
    }

}
