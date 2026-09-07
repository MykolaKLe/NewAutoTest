package ringotel.tests;

import org.openqa.selenium.remote.Browser;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ringotel.core.ApplicationManager;
import ringotel.core.TestData;
import ringotel.model.User;

public class CallTests {

    private ApplicationManager callerApp;
    private ApplicationManager calleeApp;

    private final User user1 =
            TestData.getUser1();

    private final User user2 =
            TestData.getUser2();

    @BeforeMethod
    public void setUp() {

        String browser =
                System.getProperty(
                        "browser",
                        Browser.CHROME.browserName()
                );

        callerApp =
                new ApplicationManager(
                        browser
                );

        calleeApp =
                new ApplicationManager(
                        browser
                );

        callerApp.init();
        calleeApp.init();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(
            ITestResult result
    ) {

        if (!result.isSuccess()) {

            try {

                callerApp
                        .getUser()
                        .takeScreenShot();

            } catch (Exception ignored) {
            }

            try {

                calleeApp
                        .getUser()
                        .takeScreenShot();

            } catch (Exception ignored) {
            }
        }

        if (callerApp != null) {

            try {

                if (callerApp
                        .getCall()
                        .isCallOverlayVisible()) {

                    callerApp
                            .getCall()
                            .endCall();
                }

            } catch (Exception ignored) {
            }
        }

        if (calleeApp != null) {

            try {

                if (calleeApp
                        .getCall()
                        .isCallOverlayVisible()) {

                    calleeApp
                            .getCall()
                            .endCall();
                }

            } catch (Exception ignored) {
            }
        }

        if (callerApp != null) {

            try {

                callerApp.stop();

            } catch (Exception ignored) {
            }
        }

        if (calleeApp != null) {

            try {

                calleeApp.stop();

            } catch (Exception ignored) {
            }
        }
    }

    @Test
    public void user4321Calls1234AndHangUpAfter15Seconds() {

        String callerExtension =
                user1.getUsername();

        String calleeExtension =
                user2.getUsername();

        callerApp
                .getUser()
                .login(
                        user1
                );

        calleeApp
                .getUser()
                .login(
                        user2
                );

        callerApp
                .getCall()
                .makeCallFromKeypad(
                        calleeExtension
                );

        callerApp
                .getCall()
                .waitForOutgoingCall();

        calleeApp
                .getCall()
                .waitForIncomingCall();

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .isIncomingCallVisible(),
                "Incoming call is not visible for "
                        + calleeExtension
        );

        calleeApp
                .getCall()
                .answerIncomingCall();

        callerApp
                .getCall()
                .waitForActiveCall();

        calleeApp
                .getCall()
                .waitForActiveCall();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isCallActive(),
                "Call is not active for "
                        + callerExtension
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .isCallActive(),
                "Call is not active for "
                        + calleeExtension
        );

        callerApp
                .getCall()
                .pause(
                        15000
                );

        callerApp
                .getCall()
                .endCall();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .waitUntilCallFinished(),
                "Call overlay did not disappear for "
                        + callerExtension
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .waitUntilCallFinished(),
                "Call overlay did not disappear for "
                        + calleeExtension
        );
    }

    @Test
    public void user1234Calls4321AndHangUpAfter15Seconds() {

        String callerExtension =
                user2.getUsername();

        String calleeExtension =
                user1.getUsername();

        callerApp
                .getUser()
                .login(
                        user2
                );

        calleeApp
                .getUser()
                .login(
                        user1
                );

        callerApp
                .getCall()
                .makeCallFromKeypad(
                        calleeExtension
                );

        callerApp
                .getCall()
                .waitForOutgoingCall();

        calleeApp
                .getCall()
                .waitForIncomingCall();

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .isIncomingCallVisible(),
                "Incoming call is not visible for "
                        + calleeExtension
        );

        calleeApp
                .getCall()
                .answerIncomingCall();

        callerApp
                .getCall()
                .waitForActiveCall();

        calleeApp
                .getCall()
                .waitForActiveCall();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isCallActive(),
                "Call is not active for "
                        + callerExtension
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .isCallActive(),
                "Call is not active for "
                        + calleeExtension
        );

        callerApp
                .getCall()
                .pause(
                        15000
                );

        callerApp
                .getCall()
                .endCall();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .waitUntilCallFinished(),
                "Call overlay did not disappear for "
                        + callerExtension
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .waitUntilCallFinished(),
                "Call overlay did not disappear for "
                        + calleeExtension
        );
    }
}