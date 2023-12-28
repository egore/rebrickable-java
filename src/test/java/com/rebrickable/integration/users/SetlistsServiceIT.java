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
import com.rebrickable.users.SetlistsService;
import com.rebrickable.users.model.Setlist;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetlistsServiceIT extends AbstractUsersServiceIT {

    private static SetlistsService SERVICE;

    @BeforeAll
    public static void initService() throws IOException {
        SERVICE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY"))
                .users(System.getenv("REBRICKABLE_USERNAME"), System.getenv("REBRICKABLE_PASSWORD"))
                .setlists();
    }

    @Test
    public void testCRUD() throws IOException {
        // given: A sets list
        var setlist = new Setlist();
        setlist.name = UUID.randomUUID().toString();
        setlist.buildable = true;
        setlist.numSets = 5;

        // when: the sets list is created
        Setlist created = SERVICE.create(setlist);

        // then: it now has an ID, and all fields match the given sets list
        assertThat(created.id).isNotEqualTo(0);
        assertThat(created.name).isEqualTo(setlist.name);
        assertThat(created.buildable).isEqualTo(setlist.buildable);
        assertThat(created.numSets).isEqualTo(setlist.numSets);

        // when: the sets list is retrieved
        Setlist check = SERVICE.get(created.id);

        // then: the created object could be retrieved
        assertThat(check.id).isEqualTo(created.id);
        assertThat(check.name).isEqualTo(created.name);
        assertThat(check.buildable).isEqualTo(created.buildable);
        assertThat(check.numSets).isEqualTo(created.numSets);

        // when: the sets list numSets is increased by one
        created.numSets++;
        Setlist updated = SERVICE.update(created);

        // then: it now has an ID, and all fields match the given sets list
        assertThat(updated.id).isEqualTo(created.id);
        assertThat(updated.name).isEqualTo(created.name);
        assertThat(updated.buildable).isEqualTo(created.buildable);
        assertThat(updated.numSets).isEqualTo(created.numSets);

        // when: the sets list is retrieved
        check = SERVICE.get(created.id);

        // then: the updated object could be retrieved
        assertThat(check.id).isEqualTo(updated.id);
        assertThat(check.name).isEqualTo(updated.name);
        assertThat(check.buildable).isEqualTo(updated.buildable);
        assertThat(check.numSets).isEqualTo(updated.numSets);

        SERVICE.delete(created.id);

        // when: the sets list is gone
        assertThatThrownBy(() -> SERVICE.get(created.id)).isInstanceOf(NotFoundException.class);
    }

}
