package com.vgcares.automation.tests.reviews;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Reviews List & E2E tests (doctor/reviews-data.json). */
public class ReviewsListAndE2ETest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_RV_LIST_STARTER: Add Reviews List test steps here")
    public void starterReviewsListTemplate() {
        JsonNode data = ModuleDataLoader.doctor("reviews-data.json");
        StepLogger.step("Loaded doctor/reviews-data.json (" + data.path("module").asText() + ")");
    }
}
