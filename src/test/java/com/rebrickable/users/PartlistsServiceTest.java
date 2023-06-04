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
package com.rebrickable.users;

import com.rebrickable.Rebrickable;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class PartlistsServiceTest {

    private static PartlistsService SERVICE;

    @BeforeAll
    public static void beforeAll() throws IOException {
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_API_KEY"));
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_USERNAME"));
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_PASSWORD"));
        SERVICE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).users(System.getenv("REBRICKABLE_USERNAME"), System.getenv("REBRICKABLE_PASSWORD")).partlists();
    }

    @Test
    public void testAll() throws IOException {
        Assertions.assertNotNull(SERVICE.all());
    }

    @Test
    public void testGet() throws IOException {
        Assertions.assertNotNull(SERVICE.get(SERVICE.all().iterator().next().id));
    }

    @Test
    @Disabled("Seems to hit HTTP 502 every time")
    public void testParts() throws IOException {
        Assertions.assertNotNull(SERVICE.parts(SERVICE.all().iterator().next().id));
    }

}
