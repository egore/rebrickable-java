package com.rebrickable.users;

import com.rebrickable.Rebrickable;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MinifigureServiceTest {

    private static MinifigureService SERVICE;

    @BeforeAll
    public static void beforeAll() throws IOException {
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_API_KEY"));
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_USERNAME"));
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_PASSWORD"));
        SERVICE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).users(System.getenv("REBRICKABLE_USERNAME"), System.getenv("REBRICKABLE_PASSWORD")).minifigure();
    }

    @Test
    public void testAll() throws IOException {
        Assertions.assertNotNull(SERVICE.all());
    }

}
