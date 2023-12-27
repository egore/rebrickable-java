package com.rebrickable.users;

import com.rebrickable.lego.AbstractLegoServiceTest;
import org.junit.jupiter.api.BeforeAll;

import static org.assertj.core.api.Assumptions.assumeThat;

public class AbstractUsersServiceTest extends AbstractLegoServiceTest {

    @BeforeAll
    public static void checkEnvironmentForUser() {
        assumeThat(System.getenv()).containsKey("REBRICKABLE_USERNAME");
        assumeThat(System.getenv()).containsKey("REBRICKABLE_PASSWORD");
    }
}
