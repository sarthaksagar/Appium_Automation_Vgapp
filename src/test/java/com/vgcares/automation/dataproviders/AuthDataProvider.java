package com.vgcares.automation.dataproviders;

import org.testng.annotations.DataProvider;

/**
 * Clean template DataProvider class.
 * Add role-specific or module-wise @DataProvider methods reading from
 * ModuleDataLoader.common(...), ModuleDataLoader.patient(...), or ModuleDataLoader.doctor(...).
 */
public final class AuthDataProvider {

    private AuthDataProvider() {}

    @DataProvider(name = "sampleModuleData")
    public static Object[][] sampleModuleData() {
        return new Object[][] {};
    }
}
