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
import ringotel.model.User;

public class CallControlsTests {

    private final Logger logger =
            LoggerFactory.getLogger(
                    CallControlsTests.class
            );

    private ApplicationManager callerApp;
    private ApplicationManager calleeApp;

    private final User user4321 =
            new User()
                    .setDomain("testwebsoftphone")
                    .setUsername("4321")
                    .setPassword("obFxbmYKYy9pwjAD");

    private final User user1234 =
            new User()
                    .setDomain("testwebsoftphone")
                    .setUsername("1234")
                    .setPassword("R0eE6jAMUmyck7uD");

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

        if (callerApp != null) {

            try {

                if (callerApp
                        .getCall()
                        .isCallOverlayVisible()) {

                    logger.info(
                            "Finishing remaining caller call"
                    );

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

                    logger.info(
                            "Finishing remaining callee call"
                    );

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
    public void answerCallAndCheckCallControls() {

        logger.info(
                "Logging in 4321"
        );

        callerApp
                .getUser()
                .login(
                        user4321
                );

        logger.info(
                "Logging in 1234"
        );

        calleeApp
                .getUser()
                .login(
                        user1234
                );

        logger.info(
                "4321 calls 1234"
        );

        callerApp
                .getCall()
                .makeCallFromKeypad(
                        "1234"
                );

        callerApp
                .getCall()
                .waitForOutgoingCall();

        logger.info(
                "Outgoing call detected for 4321"
        );

        calleeApp
                .getCall()
                .waitForIncomingCall();

        logger.info(
                "Incoming call detected for 1234"
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .isIncomingCallVisible(),
                "Incoming call is not visible for 1234"
        );

        calleeApp
                .getCall()
                .pause(
                        1000
                );

        logger.info(
                "1234 answers the call"
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

        logger.info(
                "Call is active for both users"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isCallActive(),
                "Call is not active for 4321"
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .isCallActive(),
                "Call is not active for 1234"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isSeparateWindowButtonAvailable(),
                "Separate window button is not available"
        );

        callerApp
                .getCall()
                .pause(
                        1000
                );

        logger.info(
                "Checking Hold"
        );

        callerApp
                .getCall()
                .holdCall();

        logger.info(
                "Hold button clicked"
        );

        callerApp
                .getCall()
                .pause(
                        3000
                );

        logger.info(
                "Checking Resume"
        );

        callerApp
                .getCall()
                .resumeCall();

        logger.info(
                "Resume button clicked"
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
                .pause(
                        1500
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

        callerApp
                .getCall()
                .pause(
                        1000
                );

        logger.info(
                "Selecting first available transfer contact"
        );

        callerApp
                .getCall()
                .selectFirstTransferContact();

        callerApp
                .getCall()
                .pause(
                        1500
                );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isBlindTransferEnabled(),
                "Blind Transfer was not enabled after selecting a contact"
        );

        Assert.assertTrue(
                callerApp
                        .getCall()
                        .isAttendedTransferEnabled(),
                "Attended Transfer was not enabled after selecting a contact"
        );

        logger.info(
                "Transfer destination selected successfully"
        );

        callerApp
                .getCall()
                .closeTransferDialog();

        callerApp
                .getCall()
                .pause(
                        1000
                );

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
                "Call overlay did not disappear for 4321"
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .waitUntilCallFinished(),
                "Call overlay did not disappear for 1234"
        );

        logger.info(
                "Call controls scenario completed successfully"
        );
    }

    @Test
    public void rejectIncomingCall() {

        logger.info(
                "Logging in 4321"
        );

        callerApp
                .getUser()
                .login(
                        user4321
                );

        logger.info(
                "Logging in 1234"
        );

        calleeApp
                .getUser()
                .login(
                        user1234
                );

        logger.info(
                "4321 calls 1234"
        );

        callerApp
                .getCall()
                .makeCallFromKeypad(
                        "1234"
                );

        callerApp
                .getCall()
                .waitForOutgoingCall();

        logger.info(
                "Outgoing call detected for 4321"
        );

        calleeApp
                .getCall()
                .waitForIncomingCall();

        logger.info(
                "Incoming call detected for 1234"
        );

        Assert.assertTrue(
                calleeApp
                        .getCall()
                        .isIncomingCallVisible(),
                "Incoming call is not visible for 1234"
        );

        calleeApp
                .getCall()
                .pause(
                        1000
                );

        logger.info(
                "1234 rejects the call"
        );

        calleeApp
                .getCall()
                .rejectIncomingCall();

        Assert.assertFalse(
                calleeApp
                        .getCall()
                        .isIncomingCallVisible(),
                "Incoming call remained visible for 1234 after Reject"
        );

        logger.info(
                "Incoming call was successfully rejected by 1234"
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
                    "Call is still visible for 4321 after Reject, possibly because it continued to voicemail"
            );

            logger.info(
                    "Ending remaining call for 4321"
            );

            callerApp
                    .getCall()
                    .endCall();

            Assert.assertTrue(
                    callerApp
                            .getCall()
                            .waitUntilCallFinished(),
                    "Remaining call could not be finished for 4321"
            );

            logger.info(
                    "Remaining call for 4321 was finished"
            );

        } else {

            logger.info(
                    "Call finished automatically for 4321 after Reject"
            );
        }

        logger.info(
                "Reject incoming call scenario completed successfully"
        );
    }
}