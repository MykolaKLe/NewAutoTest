package ringotel.tests;

import org.openqa.selenium.remote.Browser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ringotel.core.ApplicationManager;
import ringotel.core.TestData;
import ringotel.model.User;

public class CallTests {

    private final Logger logger =
            LoggerFactory.getLogger(
                    CallTests.class
            );

    private ApplicationManager user1App;
    private ApplicationManager user2App;

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

        user1App =
                new ApplicationManager(
                        browser
                );

        user2App =
                new ApplicationManager(
                        browser
                );

        user1App.init();
        user2App.init();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(
            ITestResult result
    ) {

        if (!result.isSuccess()) {

            try {

                String screenshot =
                        user1App
                                .getUser()
                                .takeScreenShot();

                logger.error(
                        "User 1 screenshot -> {}",
                        screenshot
                );

            } catch (Exception ignored) {
            }

            try {

                String screenshot =
                        user2App
                                .getUser()
                                .takeScreenShot();

                logger.error(
                        "User 2 screenshot -> {}",
                        screenshot
                );

            } catch (Exception ignored) {
            }
        }

        finishRemainingCall(
                user1App,
                "user 1"
        );

        finishRemainingCall(
                user2App,
                "user 2"
        );

        if (user1App != null) {

            try {

                user1App.stop();

            } catch (Exception ignored) {
            }
        }

        if (user2App != null) {

            try {

                user2App.stop();

            } catch (Exception ignored) {
            }
        }
    }

    @Test
    public void twoWayInternalCall() {

        String user1Extension =
                user1.getUsername();

        String user2Extension =
                user2.getUsername();

        logger.info(
                "Logging in user 1: {}",
                user1Extension
        );

        user1App
                .getUser()
                .login(
                        user1
                );

        logger.info(
                "Waiting for {} client to become ready",
                user1Extension
        );

        user1App
                .getCall()
                .waitForClientReady();

        logger.info(
                "{} client is ready",
                user1Extension
        );

        logger.info(
                "Logging in user 2: {}",
                user2Extension
        );

        user2App
                .getUser()
                .login(
                        user2
                );

        logger.info(
                "Waiting for {} client to become ready",
                user2Extension
        );

        user2App
                .getCall()
                .waitForClientReady();

        logger.info(
                "{} client is ready",
                user2Extension
        );

        runCall(
                user1App,
                user2App,
                user1Extension,
                user2Extension
        );

        logger.info(
                "Waiting for both clients to return to idle state"
        );

        user1App
                .getCall()
                .waitForClientReady();

        user2App
                .getCall()
                .waitForClientReady();

        logger.info(
                "Both clients are ready for reverse call"
        );

        user1App
                .getCall()
                .pause(
                        1500
                );

        runCall(
                user2App,
                user1App,
                user2Extension,
                user1Extension
        );

        logger.info(
                "Two-way internal call scenario completed successfully"
        );
    }

    private void runCall(
            ApplicationManager callerApp,
            ApplicationManager calleeApp,
            String callerExtension,
            String calleeExtension
    ) {

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isClientReady(),
                callerExtension
                        + " is not ready to start a call"
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .isClientReady(),
                calleeExtension
                        + " is not ready to receive a call"
        );

        logger.info(
                "{} calls {}",
                callerExtension,
                calleeExtension
        );

        callerApp
                .getCall()
                .makeCallFromKeypad(
                        calleeExtension
                );

        callerApp
                .getCall()
                .waitForOutgoingCall();

        logger.info(
                "Outgoing call detected for {}",
                callerExtension
        );

        calleeApp
                .getCall()
                .waitForIncomingCall();

        logger.info(
                "Incoming call detected for {}",
                calleeExtension
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .isIncomingCallVisible(),
                "Incoming call is not visible for "
                        + calleeExtension
        );

        logger.info(
                "{} answers the call",
                calleeExtension
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

        logger.info(
                "Call between {} and {} is active",
                callerExtension,
                calleeExtension
        );

        callerApp
                .getCall()
                .pause(
                        15000
                );

        logger.info(
                "{} ends the call",
                callerExtension
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

        logger.info(
                "Call completed successfully: {} -> {}",
                callerExtension,
                calleeExtension
        );
    }

    private void finishRemainingCall(
            ApplicationManager app,
            String side
    ) {

        if (app == null) {
            return;
        }

        try {

            if (app
                    .getCall()
                    .isCallOverlayVisible()) {

                logger.info(
                        "Finishing remaining {} call",
                        side
                );

                app
                        .getCall()
                        .endCall();
            }

        } catch (Exception ignored) {
        }
    }
}