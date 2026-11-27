package com.vgcares.automation.tests.reviews;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Reviews Summary tests (doctor/reviews-data.json). */
public class ReviewsSummaryTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_RV_SUM_STARTER: Add Reviews Summary test steps here")
    public void starterReviewsSummaryTemplate() {
        JsonNode data = ModuleDataLoader.doctor("reviews-data.json");
        StepLogger.step("Loaded doctor/reviews-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession().openReviews() and ReviewsPage
    }
}
