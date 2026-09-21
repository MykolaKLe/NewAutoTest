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

public class CallControlsTests {

    private final Logger logger =
            LoggerFactory.getLogger(
                    CallControlsTests.class
            );

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

                String screenshot =
                        callerApp
                                .getUser()
                                .takeScreenShot();

                logger.error(
                        "Caller screenshot -> {}",
                        screenshot
                );

            } catch (Exception ignored) {
            }

            try {

                String screenshot =
                        calleeApp
                                .getUser()
                                .takeScreenShot();

                logger.error(
                        "Callee screenshot -> {}",
                        screenshot
                );

            } catch (Exception ignored) {
            }
        }

        finishRemainingCall(
                callerApp,
                "caller"
        );

        finishRemainingCall(
                calleeApp,
                "callee"
        );

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
    public void answerCallAndCheckCallControls() {

        String callerExtension =
                user1.getUsername();

        String calleeExtension =
                user2.getUsername();

        loginUsersAndAnswerCall();

        logger.info(
                "Checking Separate Window button"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isSeparateWindowButtonAvailable(),
                "Separate window button is not available"
        );

        logger.info(
                "Checking Hold"
        );

        callerApp
                .getCall()
                .holdCall();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isCallOnHold(),
                "Call was not placed on hold"
        );

        logger.info(
                "Call is on hold"
        );

        callerApp
                .getCall()
                .pause(
                        1500
                );

        logger.info(
                "Checking Resume"
        );

        callerApp
                .getCall()
                .resumeCall();

        Assert.assertFalse(
                callerApp
                        .getCall()
                        .isCallOnHold(),
                "Call remained on hold after Resume"
        );

        logger.info(
                "Call resumed successfully"
        );

        callerApp
                .getCall()
                .pause(
                        1000
                );

        logger.info(
                "Checking Mute"
        );

        callerApp
                .getCall()
                .muteCall();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isMuted(),
                "Call was not muted"
        );

        logger.info(
                "Call is muted"
        );

        callerApp
                .getCall()
                .pause(
                        1500
                );

        logger.info(
                "Checking Unmute"
        );

        callerApp
                .getCall()
                .unmuteCall();

        Assert.assertFalse(
                callerApp
                        .getCall()
                        .isMuted(),
                "Call was not unmuted"
        );

        logger.info(
                "Call was unmuted successfully"
        );

        callerApp
                .getCall()
                .pause(
                        1000
                );

        logger.info(
                "Checking Record"
        );

        callerApp
                .getCall()
                .startRecording();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isRecording(),
                "Recording was not started"
        );

        logger.info(
                "Recording started"
        );

        callerApp
                .getCall()
                .pause(
                        1500
                );

        logger.info(
                "Checking Stop Recording"
        );

        callerApp
                .getCall()
                .stopRecording();

        Assert.assertFalse(
                callerApp
                        .getCall()
                        .isRecording(),
                "Recording was not stopped"
        );

        logger.info(
                "Recording stopped successfully"
        );

        callerApp
                .getCall()
                .pause(
                        1000
                );

        logger.info(
                "Opening More call actions"
        );

        callerApp
                .getCall()
                .openMoreCallActions();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isMoreActionsOpen(),
                "More call actions were not opened"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isVideoActionAvailable(),
                "Video action is not available"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isDeviceActionAvailable(),
                "Device action is not available"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isAddPartyActionAvailable(),
                "Add party action is not available"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isTranscriptActionAvailable(),
                "Transcript action is not available"
        );

        logger.info(
                "More call actions are available"
        );

        callerApp
                .getCall()
                .closeMoreCallActions();

        logger.info(
                "Opening Call Transfer"
        );

        callerApp
                .getCall()
                .openTransferDialog();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isTransferDialogOpen(),
                "Call Transfer dialog was not opened"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isTransferSearchAvailable(),
                "Transfer search is not available"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isBlindTransferButtonAvailable(),
                "Blind Transfer button is missing"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isAttendedTransferButtonAvailable(),
                "Attended Transfer button is missing"
        );

        logger.info(
                "Selecting first available transfer destination"
        );

        callerApp
                .getCall()
                .selectFirstTransferContact();

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isBlindTransferEnabled(),
                "Blind Transfer was not enabled after selecting a destination"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isAttendedTransferEnabled(),
                "Attended Transfer was not enabled after selecting a destination"
        );

        logger.info(
                "Transfer destination selected successfully"
        );

        callerApp
                .getCall()
                .closeTransferDialog();

        logger.info(
                "Ending call"
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
                "Call controls scenario completed successfully"
        );
    }

    @Test
    public void rejectIncomingCall() {

        String callerExtension =
                user1.getUsername();

        String calleeExtension =
                user2.getUsername();

        loginUsers();

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

        logger.info(
                "{} rejects the call",
                calleeExtension
        );

        calleeApp
                .getCall()
                .rejectIncomingCall();

        Assert.assertFalse(
                calleeApp
                        .getCall()
                        .isIncomingCallVisible(),
                "Incoming call remained visible for "
                        + calleeExtension
                        + " after Reject"
        );

        logger.info(
                "Incoming call was rejected by {}",
                calleeExtension
        );

        callerApp
                .getCall()
                .pause(
                        3000
                );

        if (callerApp
                .getCall()
                .isCallOverlayVisible()) {

            logger.info(
                    "Call remains visible for {} after Reject",
                    callerExtension
            );

            logger.info(
                    "Ending remaining caller call"
            );

            callerApp
                    .getCall()
                    .endCall();

            Assert.assertTrue(
                    callerApp
                            .getCall()
                            .waitUntilCallFinished(),
                    "Remaining call could not be finished for "
                            + callerExtension
            );

        } else {

            logger.info(
                    "Call finished automatically for {} after Reject",
                    callerExtension
            );
        }

        logger.info(
                "Reject incoming call scenario completed successfully"
        );
    }

    private void loginUsersAndAnswerCall() {

        String callerExtension =
                user1.getUsername();

        String calleeExtension =
                user2.getUsername();

        loginUsers();

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
                "Call is active for {} and {}",
                callerExtension,
                calleeExtension
        );
    }

    private void loginUsers() {

        String callerExtension =
                user1.getUsername();

        String calleeExtension =
                user2.getUsername();

        logger.info(
                "Logging in caller {}",
                callerExtension
        );

        callerApp
                .getUser()
                .login(
                        user1
                );

        logger.info(
                "Waiting for caller {} client readiness",
                callerExtension
        );

        callerApp
                .getCall()
                .waitForClientReady();

        logger.info(
                "Caller {} client is ready",
                callerExtension
        );

        logger.info(
                "Logging in callee {}",
                calleeExtension
        );

        calleeApp
                .getUser()
                .login(
                        user2
                );

        logger.info(
                "Waiting for callee {} client readiness",
                calleeExtension
        );

        calleeApp
                .getCall()
                .waitForClientReady();

        logger.info(
                "Callee {} client is ready",
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