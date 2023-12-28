package com.rebrickable.integration.lego;

import org.junit.jupiter.api.BeforeAll;

import static org.assertj.core.api.Assumptions.assumeThat;

public class AbstractLegoServiceIT {

    @BeforeAll
    public static void checkEnvironment() {
        assumeThat(System.getenv()).containsKey("REBRICKABLE_API_KEY");
    }

}
