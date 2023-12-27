package com.rebrickable.users;

import com.rebrickable.lego.AbstractLegoServiceTest;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;

public class AbstractUsersServiceTest extends AbstractLegoServiceTest {

    @BeforeAll
    public static void checkEnvironmentForUser() {
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_USERNAME"));
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_PASSWORD"));
    }
}
