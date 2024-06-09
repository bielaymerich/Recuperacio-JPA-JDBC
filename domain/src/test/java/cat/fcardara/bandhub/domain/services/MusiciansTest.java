package cat.fcardara.bandhub.domain.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import cat.fcardara.bandhub.domain.ModelFactory;

public class MusiciansTest {
    @Test
    void testGetNew() {
        assertNotNull((new ModelFactory()).newMusician());
    }
}
