package com.vgcares.automation.base;

import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.driver.MobileInfrastructureException;
import com.vgcares.automation.listeners.TestListener;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.pages.patient.PatientHomePage;
import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.workflows.common.OnboardingWorkflow;
import com.vgcares.automation.workflows.doctor.DoctorAuthWorkflow;
import com.vgcares.automation.workflows.patient.PatientAuthWorkflow;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public abstract class BaseTest {
    public static final String SUITE_SETUP_FAILURE = BaseTest.class.getName() + ".suiteSetupFailure";

    protected final Logger log = LogManager.getLogger(getClass());
    protected AndroidDriver driver;

    @BeforeSuite(alwaysRun = true)
    public final void startMobileSuite(ITestContext context) {
        try {
            var suite = context.getCurrentXmlTest().getSuite();
            if (suite.getParallel().isParallel() || suite.getTests().stream().anyMatch(test -> test.getParallel().isParallel())) {
                throw new MobileInfrastructureException("Driver configuration",
                        "Parallel TestNG execution is incompatible with the shared physical Android session.");
            }
            DriverManager.startSession();
        } catch (RuntimeException e) {
            context.getSuite().setAttribute(SUITE_SETUP_FAILURE, e);
            throw e;
        }
    }

    @BeforeClass(alwaysRun = true)
    public final void setUp(ITestContext context) {
        if (context != null && context.getSuite().getAttribute(SUITE_SETUP_FAILURE) != null) {
            return;
        }
        if (!DriverManager.hasActiveSession()) {
            DriverManager.startSession();
        }
        driver = getDriver();
    }

    @AfterSuite(alwaysRun = true)
    public final void finishMobileSuite() {
        DriverManager.quitDriver();
    }

    protected final LoginPage openFreshLogin() {
        prepareAuthScreen();
        return new LoginPage(getDriver()).openLoginTab();
    }

    protected final void prepareAuthScreen() {
        OnboardingWorkflow.ensureAuthScreenReady(getDriver());
    }

    protected final HomePage loginAsQaDoctor(LoginPage loginPage) {
        QaCredentials account = QaCredentials.doctor();
        return DoctorAuthWorkflow.loginAsDoctor(getDriver(), account.email(), account.password());
    }

    protected final HomePage loginAsQaDoctor() {
        QaCredentials account = QaCredentials.doctor();
        HomePage homePage = DoctorAuthWorkflow.ensureAuthenticatedDoctor(getDriver(), account.email(), account.password());
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "The authorized QA doctor account must reach the doctor dashboard.");
        return homePage;
    }

    protected final PatientHomePage loginAsQaPatient(LoginPage loginPage) {
        QaCredentials account = QaCredentials.patient();
        return PatientAuthWorkflow.loginAsPatient(getDriver(), account.email(), account.password());
    }

    protected final PatientHomePage loginAsQaPatient() {
        QaCredentials account = QaCredentials.patient();
        PatientHomePage patientHome = PatientAuthWorkflow.ensureAuthenticatedPatient(
                getDriver(), account.email(), account.password());
        Assert.assertTrue(patientHome.isPatientDashboardLoaded(),
                "The authorized QA patient account must reach the patient dashboard.");
        return patientHome;
    }

    protected final AndroidDriver getDriver() {
        return DriverManager.getDriver();
    }
}
