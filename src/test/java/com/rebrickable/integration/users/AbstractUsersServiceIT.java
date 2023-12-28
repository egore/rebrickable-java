package com.rebrickable.integration.users;

import com.rebrickable.integration.lego.AbstractLegoServiceIT;
import org.junit.jupiter.api.BeforeAll;

import static org.assertj.core.api.Assumptions.assumeThat;

public class AbstractUsersServiceIT extends AbstractLegoServiceIT {

    @BeforeAll
    public static void checkEnvironmentForUser() {
        assumeThat(System.getenv()).containsKey("REBRICKABLE_USERNAME");
        assumeThat(System.getenv()).containsKey("REBRICKABLE_PASSWORD");
    }
}
