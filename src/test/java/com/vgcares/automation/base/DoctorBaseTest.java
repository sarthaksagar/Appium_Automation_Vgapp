package com.vgcares.automation.base;

import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.utils.ExecutionUtils;
import org.testng.annotations.BeforeClass;

/**
 * Base test class for Doctor-role mobile automation tests.
 * Ensures active execution role is tagged as DOCTOR and enforces clean Doctor auth state.
 */
public abstract class DoctorBaseTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public final void assignDoctorRole() {
        ExecutionUtils.setActiveRole(UserRole.DOCTOR);
    }

    protected final HomePage ensureDoctorSession() {
        return loginAsQaDoctor();
    }
}
