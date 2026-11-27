package com.vgcares.automation.base;

import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.pages.patient.PatientHomePage;
import com.vgcares.automation.utils.ExecutionUtils;
import org.testng.annotations.BeforeClass;

/**
 * Base test class for Patient-role mobile automation tests.
 * Ensures active execution role is tagged as PATIENT and enforces clean Patient auth state.
 */
public abstract class PatientBaseTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public final void assignPatientRole() {
        ExecutionUtils.setActiveRole(UserRole.PATIENT);
    }

    protected final PatientHomePage ensurePatientSession() {
        return new PatientHomePage(getDriver());
    }
}
