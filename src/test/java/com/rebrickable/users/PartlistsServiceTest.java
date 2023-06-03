package com.rebrickable.users;

import com.rebrickable.Rebrickable;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class PartlistsServiceTest {

    private static PartlistsService SERVICE;

    @BeforeAll
    public static void beforeAll() throws IOException {
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_API_KEY"));
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_USERNAME"));
        Assumptions.assumeTrue(System.getenv().containsKey("REBRICKABLE_PASSWORD"));
        SERVICE = new Rebrickable(System.getenv("REBRICKABLE_API_KEY")).users(System.getenv("REBRICKABLE_USERNAME"), System.getenv("REBRICKABLE_PASSWORD")).partlists();
    }

    @Test
    public void testAll() throws IOException {
        Assertions.assertNotNull(SERVICE.all());
    }

    @Test
    public void testGet() throws IOException {
        Assertions.assertNotNull(SERVICE.get(SERVICE.all().iterator().next().id));
    }

    @Test
    @Disabled("Seems to hit HTTP 502 every time")
    public void testParts() throws IOException {
        Assertions.assertNotNull(SERVICE.parts(SERVICE.all().iterator().next().id));
    }

}
