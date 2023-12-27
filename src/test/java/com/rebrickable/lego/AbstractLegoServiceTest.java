package com.rebrickable.lego;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;

public class AbstractLegoServiceTest {

    @BeforeAll
    public static void checkEnvironment() {
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_API_KEY"));
    }

}
