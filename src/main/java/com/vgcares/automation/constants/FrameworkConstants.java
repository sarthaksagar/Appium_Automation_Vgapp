package com.vgcares.automation.constants;

import java.io.File;

public final class FrameworkConstants {

    private FrameworkConstants() {}

    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String CONFIG_FILE_PATH = USER_DIR + File.separator + "src" + File.separator 
            + "main" + File.separator + "resources" + File.separator + "config.properties";

    public static final String TEST_DATA_JSON_PATH = USER_DIR + File.separator + "src" + File.separator 
            + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator + "auth-data.json";

    public static final String REPORTS_DIR = USER_DIR + File.separator + "reports";
    public static final String SCREENSHOTS_DIR = USER_DIR + File.separator + "screenshots";
    public static final String LOGS_DIR = USER_DIR + File.separator + "logs";

    public static final int DEFAULT_EXPLICIT_WAIT = 8;
    public static final int POLLING_INTERVAL_MILLIS = 200;
}
