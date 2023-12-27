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
import com.rebrickable.lego.exceptions.NotFoundException;
import com.rebrickable.users.model.Partlist;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.UUID;

public class PartlistsServiceTest extends AbstractUsersServiceTest {

    private static PartlistsService SERVICE;

    @BeforeAll
    public static void initService() throws IOException {
        SERVICE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY"))
                .users(System.getenv("REBRICKABLE_USERNAME"), System.getenv("REBRICKABLE_PASSWORD"))
                .partlists();
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

    @Test
    public void testCRUD() throws IOException {
        // given: A parts list
        var partslist = new Partlist();
        partslist.name = UUID.randomUUID().toString();
        partslist.buildable = true;
        partslist.numParts = 5;

        // when: the part is created
        Partlist created = SERVICE.create(partslist);

        // then: it now has an ID, and all fields match the given part
        Assertions.assertNotEquals(created.id, 0);
        Assertions.assertEquals(created.name, partslist.name);
        Assertions.assertEquals(created.buildable, partslist.buildable);
        Assertions.assertEquals(created.numParts, partslist.numParts);

        // when: the partslist is retrieved
        Partlist check = SERVICE.get(created.id);

        // then: the created object could be retrieved
        Assertions.assertEquals(check.id, created.id);
        Assertions.assertEquals(check.name, created.name);
        Assertions.assertEquals(check.buildable, created.buildable);
        Assertions.assertEquals(check.numParts, created.numParts);

        // when: the part numParts is increased by one
        created.numParts++;
        Partlist updated = SERVICE.update(created);

        // then: it now has an ID, and all fields match the given part
        Assertions.assertEquals(updated.id, created.id);
        Assertions.assertEquals(updated.name, created.name);
        Assertions.assertEquals(updated.buildable, created.buildable);
        Assertions.assertEquals(updated.numParts, created.numParts);

        // when: the partslist is retrieved
        check = SERVICE.get(created.id);

        // then: the updated object could be retrieved
        Assertions.assertEquals(check.id, updated.id);
        Assertions.assertEquals(check.name, updated.name);
        Assertions.assertEquals(check.buildable, updated.buildable);
        Assertions.assertEquals(check.numParts, updated.numParts);

        SERVICE.delete(created.id);

        // when: the partslist is gone
        Assertions.assertThrows(NotFoundException.class, () -> SERVICE.get(created.id));
    }

}
