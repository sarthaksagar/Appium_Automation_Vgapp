package com.vgcares.automation.dataproviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.constants.FrameworkConstants;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.annotations.DataProvider;

public final class AuthDataProvider {

    private AuthDataProvider() {}

    // ── Password Mismatch ─────────────────────────────────────────────────
    @DataProvider(name = "passwordMismatchData")
    public static Object[][] getPasswordMismatchData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("passwordMismatch")) {
            JsonNode d = root.get("passwordMismatch");
            return new Object[][] {{
                d.path("fullName").asText(),
                d.path("email").asText(),
                d.path("password").asText(),
                d.path("confirmPassword").asText()
            }};
        }
        return new Object[][] {{"Mismatch Tester", "mismatch.test@example.com", "Password@123", "WrongPassword@456"}};
    }

    // ── Invalid Email Formats (multi-row) ─────────────────────────────────
    @DataProvider(name = "invalidEmailData")
    public static Object[][] getInvalidEmailData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("invalidEmails") && root.get("invalidEmails").isArray()) {
            JsonNode arr = root.get("invalidEmails");
            Object[][] data = new Object[arr.size()][4];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{
                    "John Doe",
                    arr.get(i).path("email").asText(),
                    "Test@1234",
                    "Test@1234"
                };
            }
            return data;
        }
        // Fallback: 5 hard-coded invalid formats
        return new Object[][] {
            {"John Doe", "invalidemail",    "Test@1234", "Test@1234"},
            {"John Doe", "test@",           "Test@1234", "Test@1234"},
            {"John Doe", "test@.com",       "Test@1234", "Test@1234"},
            {"John Doe", "test @email.com", "Test@1234", "Test@1234"},
            {"John Doe", "@email.com",      "Test@1234", "Test@1234"}
        };
    }

    // ── Weak Password Variations (multi-row) ──────────────────────────────
    @DataProvider(name = "weakPasswordData")
    public static Object[][] getWeakPasswordData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("weakPasswords") && root.get("weakPasswords").isArray()) {
            JsonNode arr = root.get("weakPasswords");
            Object[][] data = new Object[arr.size()][4];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{
                    "John Doe",
                    "test.weak" + i + "@email.com",
                    arr.get(i).path("password").asText(),
                    arr.get(i).path("password").asText()
                };
            }
            return data;
        }
        return new Object[][] {
            {"John Doe", "test1@email.com", "Test@12",  "Test@12"},
            {"John Doe", "test2@email.com", "test@1234","test@1234"},
            {"John Doe", "test3@email.com", "TEST@1234","TEST@1234"},
            {"John Doe", "test4@email.com", "Test@abcd","Test@abcd"},
            {"John Doe", "test5@email.com", "Test1234", "Test1234"}
        };
    }

    // ── Invalid Full Name Variations (multi-row) ──────────────────────────
    @DataProvider(name = "invalidNameData")
    public static Object[][] getInvalidNameData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("invalidNames") && root.get("invalidNames").isArray()) {
            JsonNode arr = root.get("invalidNames");
            Object[][] data = new Object[arr.size()][4];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{
                    arr.get(i).path("name").asText(),
                    "test.name" + i + "@email.com",
                    "Test@1234",
                    "Test@1234"
                };
            }
            return data;
        }
        return new Object[][] {
            {"John123",   "test1@email.com", "Test@1234", "Test@1234"},
            {"John@Doe",  "test2@email.com", "Test@1234", "Test@1234"},
            {"J",         "test3@email.com", "Test@1234", "Test@1234"}
        };
    }

    // ── Login Invalid Email Formats ───────────────────────────────────────
    @DataProvider(name = "loginInvalidEmailData")
    public static Object[][] getLoginInvalidEmailData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("loginInvalidEmails") && root.get("loginInvalidEmails").isArray()) {
            JsonNode arr = root.get("loginInvalidEmails");
            Object[][] data = new Object[arr.size()][2];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{
                    arr.get(i).path("email").asText(),
                    "Valid@1234"
                };
            }
            return data;
        }
        return new Object[][] {
            {"testemail.com", "Valid@1234"},
            {"test@",         "Valid@1234"},
            {"test@.com",     "Valid@1234"}
        };
    }

    // ── All 10 Languages ─────────────────────────────────────────────────
    @DataProvider(name = "allLanguagesData")
    public static Object[][] getAllLanguagesData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("languages") && root.get("languages").isArray()) {
            JsonNode arr = root.get("languages");
            Object[][] data = new Object[arr.size()][1];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{ arr.get(i).asText() };
            }
            return data;
        }
        return new Object[][] {
            {"English"}, {"Hindi"}, {"French"}, {"Russian"}, {"Spanish"},
            {"Portuguese"}, {"German"}, {"Swedish"}, {"Vietnamese"}, {"Uzbek"}
        };
    }

    // ── Forgot Password Invalid Emails ────────────────────────────────────
    @DataProvider(name = "forgotPasswordInvalidEmailData")
    public static Object[][] getForgotPasswordInvalidEmailData() {
        return new Object[][] {
            {"invalidemail",           "No @ symbol"},
            {"test@",                  "No domain"},
            {"test @email.com",        "Contains space"},
            {"notregistered@email.com","Not registered email"}
        };
    }

    // ── Home / Dashboard Patient Search Data ──────────────────────────────
    @DataProvider(name = "patientSearchData")
    public static Object[][] getPatientSearchData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("homeData")) {
            JsonNode homeData = root.get("homeData");
            if (homeData.has("patientSearchTerms") && homeData.get("patientSearchTerms").isArray()) {
                JsonNode arr = homeData.get("patientSearchTerms");
                Object[][] data = new Object[arr.size()][1];
                for (int i = 0; i < arr.size(); i++) {
                    data[i] = new Object[]{ arr.get(i).asText() };
                }
                return data;
            }
        }
        return new Object[][] {
            {"Tony Deshpande"},
            {"Prateek"},
            {"Riya"}
        };
    }

    // ── Hospital Chips Data ───────────────────────────────────────────────
    @DataProvider(name = "hospitalChipsData")
    public static Object[][] getHospitalChipsData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("homeData")) {
            JsonNode homeData = root.get("homeData");
            if (homeData.has("hospitals") && homeData.get("hospitals").isArray()) {
                JsonNode arr = homeData.get("hospitals");
                Object[][] data = new Object[arr.size()][1];
                for (int i = 0; i < arr.size(); i++) {
                    data[i] = new Object[]{ arr.get(i).asText() };
                }
                return data;
            }
        }
        return new Object[][] {
            {"All Hospitals"},
            {"MGM Hospital"},
            {"Jehangir Hospital"}
        };
    }

    // ── Bottom Navigation Tabs ────────────────────────────────────────────
    @DataProvider(name = "bottomNavTabsData")
    public static Object[][] getBottomNavTabsData() {
        return new Object[][] {
            {"home"},
            {"availability"},
            {"appointments"},
            {"messages"}
        };
    }

    // ── Doctor Negative Login Scenarios (Data-driven) ──────────────────────
    @DataProvider(name = "doctorNegativeLoginData")
    public static Object[][] getDoctorNegativeLoginData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("doctorLoginNegativeCases") && root.get("doctorLoginNegativeCases").isArray()) {
            JsonNode arr = root.get("doctorLoginNegativeCases");
            Object[][] data = new Object[arr.size()][4];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{
                    arr.get(i).path("id").asText(),
                    arr.get(i).path("email").asText(),
                    arr.get(i).path("password").asText(),
                    arr.get(i).path("description").asText()
                };
            }
            return data;
        }
        return new Object[][] {
            {"TC_ID_DOC_LOG-001", "", "Fixture-Password9!", "Empty email"},
            {"TC_ID_DOC_LOG-002", "unregistered-doctor@example.invalid", "", "Empty password"},
            {"TC_ID_DOC_LOG-003", "invalid.doctor@vgcares.com", "WrongPassword@123", "Non-existent account"},
            {"TC_ID_DOC_LOG-004", "doctor.prateek", "Fixture-Password9!", "Malformed email without @"}
        };
    }

    // ── Doctor Navigation Drawer Items ────────────────────────────────────
    @DataProvider(name = "doctorDrawerItemsData")
    public static Object[][] getDoctorDrawerItemsData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("doctorNavigationDrawer") && root.get("doctorNavigationDrawer").isArray()) {
            JsonNode arr = root.get("doctorNavigationDrawer");
            Object[][] data = new Object[arr.size()][1];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{ arr.get(i).asText() };
            }
            return data;
        }
        return new Object[][] {
            {"My Profile"},
            {"Hospital Affiliations"},
            {"Surgery Requests"},
            {"My Consultation Fees"},
            {"My Reviews"},
            {"My Documents"},
            {"Settings"},
            {"Logout"}
        };
    }

    // ── Messages Search Negative Data ─────────────────────────────────────
    @DataProvider(name = "messagesSearchNegativeData")
    public static Object[][] getMessagesSearchNegativeData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("messagesData") && root.get("messagesData").has("searchEdgeCases")) {
            JsonNode arr = root.get("messagesData").get("searchEdgeCases");
            Object[][] data = new Object[arr.size()][3];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{
                    arr.get(i).path("id").asText(),
                    arr.get(i).path("query").asText(),
                    arr.get(i).path("description").asText()
                };
            }
            return data;
        }
        return new Object[][] {
            {"TC_ID_MSG-NEG-017", "MGM", "Immediate clear"},
            {"TC_ID_MSG-NEG-019", "MGM\nHospital", "Multiline query"},
            {"TC_ID_MSG-NEG-021", "ＭＧＭ", "Unicode variant"}
        };
    }

    // ── Messages Composer Edge & Negative Payloads ────────────────────────
    @DataProvider(name = "messagesComposerNegativeData")
    public static Object[][] getMessagesComposerNegativeData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("messagesData") && root.get("messagesData").has("composerEdgeCases")) {
            JsonNode arr = root.get("messagesData").get("composerEdgeCases");
            Object[][] data = new Object[arr.size()][3];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{
                    arr.get(i).path("id").asText(),
                    arr.get(i).path("payload").asText(),
                    arr.get(i).path("description").asText()
                };
            }
            return data;
        }
        return new Object[][] {
            {"TC_ID_MSG-NEG-030", "", "Empty message send"},
            {"TC_ID_MSG-NEG-048", "\n\n\n", "Newlines only"},
            {"TC_ID_MSG-NEG-050", "مرحبا Hello Dr. Prateek", "Mixed RTL/LTR"}
        };
    }

    // ── Messages Attachment Edge Cases ────────────────────────────────────
    @DataProvider(name = "messagesAttachmentNegativeData")
    public static Object[][] getMessagesAttachmentNegativeData() {
        JsonNode root = JsonUtils.readJsonTree(FrameworkConstants.TEST_DATA_JSON_PATH);
        if (root != null && root.has("messagesData") && root.get("messagesData").has("attachmentEdgeCases")) {
            JsonNode arr = root.get("messagesData").get("attachmentEdgeCases");
            Object[][] data = new Object[arr.size()][3];
            for (int i = 0; i < arr.size(); i++) {
                data[i] = new Object[]{
                    arr.get(i).path("id").asText(),
                    arr.get(i).path("fileName").asText(),
                    arr.get(i).path("description").asText()
                };
            }
            return data;
        }
        return new Object[][] {
            {"TC_ID_MSG-NEG-054", "very_long_300_character_filename.pdf", "Long filename"},
            {"TC_ID_MSG-NEG-055", "fake.pdf", "Mismatched extension"},
            {"TC_ID_MSG-NEG-056", "document.pdf.exe", "Double extension"}
        };
    }

    // ── Slots Affiliated Hospitals Data ───────────────────────────────────
    @DataProvider(name = "slotHospitalsData")
    public static Object[][] getSlotHospitalsData() {
        return new Object[][] {
            {"MGM Hospital"},
            {"Jehangir Hospital"},
            {"Anand Rishi Hospital"}
        };
    }

}
