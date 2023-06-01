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

import org.junit.jupiter.api.*;

import java.io.IOException;

public class PartServiceTest {

    @BeforeAll
    public static void beforeAll() {
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_API_KEY"));
    }

    @Test
    @Disabled("Loads to much data")
    public void testAll() throws IOException {
        Assertions.assertNotNull(new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).part().all());
    }

    @Test
    public void testAllPage() throws IOException {
        Assertions.assertNotNull(new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).part().all(1, 1));
    }

    @Test
    public void testGet() throws IOException {
        Assertions.assertNotNull(new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).part().get("3069b"));
        Assertions.assertNotNull(new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).part().get("3069a"));
        Assertions.assertNotNull(new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).part().get("973c27h27"));
        Assertions.assertNotNull(new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).part().get("3004"));
    }

    @Test
    public void testColors() throws IOException {
        Assertions.assertNotNull(new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).part().colors("973c27h27"));
    }

    @Test
    public void testColor() throws IOException {
        Assertions.assertNotNull(new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).part().color("973c27h27", 15));
    }

    @Test
    public void testColorSet() throws IOException {
        Assertions.assertNotNull(new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).part().colorSet("973c27h27", 15));
    }

}
