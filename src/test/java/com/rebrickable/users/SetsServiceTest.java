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
import com.rebrickable.lego.SetService;
import com.rebrickable.lego.exceptions.NotFoundException;
import com.rebrickable.lego.model.Set;
import com.rebrickable.users.model.SetElement;
import com.rebrickable.users.model.Setlist;
import com.rebrickable.users.responses.SetCreateResponse;
import com.rebrickable.users.responses.SetUpdateResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetsServiceTest extends AbstractUsersServiceTest {

    private static SetService SET_SERVICE;
    private static SetsService SERVICE;

    @BeforeAll
    public static void initService() throws IOException {
        Rebrickable rebrickable = new Rebrickable(System.getenv("REBRICKABLE_API_KEY"));
        SET_SERVICE = rebrickable
                .lego()
                .set();
        SERVICE = rebrickable
                .users(System.getenv("REBRICKABLE_USERNAME"), System.getenv("REBRICKABLE_PASSWORD"))
                .sets();
    }

    @Test
    public void testCRUD() throws IOException {

        int countBefore = SERVICE.all().size();

        // given: A set
        var set = new SetElement();
        set.quantity = 1;
        set.set = SET_SERVICE.page(1, 1).iterator().next();
        set.includeSpares = true;

        // when: the set is created
        SetCreateResponse created = SERVICE.create(set);

        // then: it now has an ID, and all fields match the given set
        assertThat(created.quantity).isEqualTo(set.quantity);

        // when: the set is retrieved
        SetElement check = SERVICE.get(created.setNum);

        // then: the created object could be retrieved
        assertThat(check.quantity).isEqualTo(created.quantity);

        // when: the set numSets is increased by one
        set.quantity++;
        SetUpdateResponse updated = SERVICE.update(set);

        // then: it now has an ID, and all fields match the given set
        assertThat(updated.quantity).isEqualTo(set.quantity);

        // when: the set is retrieved
        check = SERVICE.get(created.setNum);

        // then: the updated object could be retrieved
        assertThat(check.quantity).isEqualTo(updated.quantity);

        SERVICE.delete(created.setNum);

        // when: the set is gone
        assertThatThrownBy(() -> SERVICE.get(created.setNum)).isInstanceOf(NotFoundException.class);

        int countAfter = SERVICE.all().size();
        assertThat(countBefore).isEqualTo(countAfter);
    }

}
