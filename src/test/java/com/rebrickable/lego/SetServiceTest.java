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
import org.junit.jupiter.api.*;

import java.io.IOException;

public class SetServiceTest extends AbstractLegoServiceTest {

    private static SetService SERVICE;

    @BeforeAll
    public static void initService() {
        SERVICE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY"))
                .lego()
                .set();
    }

    @Test
    @Disabled("Loads to much data")
    public void testAll() throws IOException {
        Assertions.assertNotNull(SERVICE.all());
    }

    @Test
    public void testAllPage() throws IOException {
        Assertions.assertNotNull(SERVICE.all(1, 1));
    }

    @Test
    public void testGet() throws IOException {
        Assertions.assertNotNull(SERVICE.get("71236"));
    }

    @Test
    public void testAlternates() throws IOException {
        Assertions.assertNotNull(SERVICE.alternates("9476-1"));
    }

    @Test
    public void testMinifigures() throws IOException {
        Assertions.assertNotNull(SERVICE.minifigures("9476"));
    }

    @Test
    public void testParts() throws IOException {
        Assertions.assertNotNull(SERVICE.parts("9476"));
    }

    @Test
    public void testSets() throws IOException {
        Assertions.assertNotNull(SERVICE.sets("9476"));
    }

}
