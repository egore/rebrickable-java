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
import com.rebrickable.lego.model.SetPart;
import com.rebrickable.users.model.LostPart;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

public class LostPartsServiceTest extends AbstractUsersServiceTest {

    private static SetService SET_SERVICE;
    private static LostPartsService SERVICE;

    @BeforeAll
    public static void initService() throws IOException {
        Rebrickable REBRICKABLE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY"));
        SET_SERVICE = REBRICKABLE
                .lego()
                .set();
        SERVICE = REBRICKABLE
                .users(System.getenv("REBRICKABLE_USERNAME"), System.getenv("REBRICKABLE_PASSWORD"))
                .lostParts();
    }

    @Test
    public void testAddDelete() throws IOException {
        // given: a (possible empty) list of lost parts
        List<LostPart> before = SERVICE.all();
        long countBefore = before.size();

        // given: a LEGO set with parts
        List<SetPart> parts = SET_SERVICE.parts("0241357594-1");
        assertThat(parts).isNotNull();
        assertThat(parts).isNotEmpty();

        // given: a part of the LEGO set
        SetPart part = parts.get(0);

        // given: the part chosen must not yet be lost
        for (var p : before) {
            assumeThat(p.part.inventoryPartId).isNotEqualTo(part.inventoryPartId);
        }

        // when: the part is lost
        LostPart added = SERVICE.add(part.inventoryPartId, 1);
        // then: a lost part is created
        assertThat(added).isNotNull();

        // then: the amount of lost parts is increased by one
        List<LostPart> afterAdding = SERVICE.all();
        long countAfterAdding = afterAdding.size();
        assertThat(countAfterAdding).isEqualTo(countBefore + 1);

        // when: the lost part is removed
        SERVICE.delete(added.id);

        // then: the amount of lost parts is decreased by one and back to the original value
        List<LostPart> afterDeletion = SERVICE.all();
        long countAfterDeletion = afterDeletion.size();
        assertThat(countAfterDeletion).isEqualTo(countBefore);
    }

}
