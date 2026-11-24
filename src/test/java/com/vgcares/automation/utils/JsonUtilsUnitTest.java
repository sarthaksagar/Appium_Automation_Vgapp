package com.vgcares.automation.utils;

import com.vgcares.automation.constants.FrameworkConstants;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

public class JsonUtilsUnitTest {
    @Test
    public void readsTheExistingAuthFixtures() {
        assertTrue(JsonUtils.readJsonFile(FrameworkConstants.TEST_DATA_JSON_PATH).path("languages").isArray());
    }

    @Test
    public void reportsMissingDataWithTheCause() {
        String missingFile = Path.of("src", "test", "resources", "missing-json-fixture.json").toString();
        UncheckedIOException failure = expectThrows(UncheckedIOException.class,
                () -> JsonUtils.readJsonTree(missingFile));
        assertTrue(failure.getMessage().contains(missingFile));
        assertTrue(failure.getCause() instanceof FileNotFoundException);
    }
}
