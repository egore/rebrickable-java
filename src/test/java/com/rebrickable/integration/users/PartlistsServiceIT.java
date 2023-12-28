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
package com.rebrickable.integration.users;

import com.rebrickable.Rebrickable;
import com.rebrickable.lego.exceptions.NotFoundException;
import com.rebrickable.users.PartlistsService;
import com.rebrickable.users.model.Partlist;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PartlistsServiceIT extends AbstractUsersServiceIT {

    private static PartlistsService SERVICE;

    @BeforeAll
    public static void initService() throws IOException {
        SERVICE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY"))
                .users(System.getenv("REBRICKABLE_USERNAME"), System.getenv("REBRICKABLE_PASSWORD"))
                .partlists();
    }

    @Test
    public void testAll() throws IOException {
        assertThat(SERVICE.all()).isNotNull();
    }

    @Test
    public void testGet() throws IOException {
        assertThat(SERVICE.get(SERVICE.page(1, 1).iterator().next().id)).isNotNull();
    }

    @Test
    @Disabled("Seems to hit HTTP 502 every time")
    public void testParts() throws IOException {
        assertThat(SERVICE.parts(SERVICE.page(1, 1).iterator().next().id)).isNotNull();
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
        assertThat(created.id).isNotEqualTo(0);
        assertThat(created.name).isEqualTo(partslist.name);
        assertThat(created.buildable).isEqualTo(partslist.buildable);
        assertThat(created.numParts).isEqualTo(partslist.numParts);

        // when: the partslist is retrieved
        Partlist check = SERVICE.get(created.id);

        // then: the created object could be retrieved
        assertThat(check.id).isEqualTo(created.id);
        assertThat(check.name).isEqualTo(created.name);
        assertThat(check.buildable).isEqualTo(created.buildable);
        assertThat(check.numParts).isEqualTo(created.numParts);

        // when: the part numParts is increased by one
        created.numParts++;
        Partlist updated = SERVICE.update(created);

        // then: it now has an ID, and all fields match the given part
        assertThat(updated.id).isEqualTo(created.id);
        assertThat(updated.name).isEqualTo(created.name);
        assertThat(updated.buildable).isEqualTo(created.buildable);
        assertThat(updated.numParts).isEqualTo(created.numParts);

        // when: the partslist is retrieved
        check = SERVICE.get(created.id);

        // then: the updated object could be retrieved
        assertThat(check.id).isEqualTo(updated.id);
        assertThat(check.name).isEqualTo(updated.name);
        assertThat(check.buildable).isEqualTo(updated.buildable);
        assertThat(check.numParts).isEqualTo(updated.numParts);

        SERVICE.delete(created.id);

        // when: the partslist is gone
        assertThatThrownBy(() -> SERVICE.get(created.id)).isInstanceOf(NotFoundException.class);
    }

}
