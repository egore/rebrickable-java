package com.rebrickable.lego;

import org.junit.jupiter.api.BeforeAll;

import static org.assertj.core.api.Assumptions.assumeThat;

public class AbstractLegoServiceTest {

    @BeforeAll
    public static void checkEnvironment() {
        assumeThat(System.getenv()).containsKey("REBRICKABLE_API_KEY");
    }

}
