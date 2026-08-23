package ringotel.tests;

import org.openqa.selenium.remote.Browser;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ringotel.core.ApplicationManager;
import ringotel.model.User;

public class CallTests {

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
                new ApplicationManager(browser);

        calleeApp =
                new ApplicationManager(browser);

        callerApp.init();
        calleeApp.init();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        if (!result.isSuccess()) {

            try {
                callerApp.getUser().takeScreenShot();
            } catch (Exception ignored) {
            }

            try {
                calleeApp.getUser().takeScreenShot();
            } catch (Exception ignored) {
            }
        }

        if (callerApp != null) {
            callerApp.stop();
        }

        if (calleeApp != null) {
            calleeApp.stop();
        }
    }

    @Test
    public void user4321Calls1234AndHangUpAfter15Seconds() {

        callerApp.getUser().login(user4321);
        calleeApp.getUser().login(user1234);

        callerApp.getCall().makeCallFromKeypad("1234");

        callerApp.getCall().waitForOutgoingCall();
        calleeApp.getCall().waitForIncomingCall();

        Assert.assertTrue(
                callerApp.getCall().isCallActive()
        );

        Assert.assertTrue(
                calleeApp.getCall().isCallActive()
        );

        callerApp.getCall().pause(15000);

        callerApp.getCall().endCall();

        Assert.assertTrue(
                callerApp.getCall().waitUntilCallFinished()
        );

        Assert.assertTrue(
                calleeApp.getCall().waitUntilCallFinished()
        );
    }

    @Test
    public void user1234Calls4321AndHangUpAfter15Seconds() {

        callerApp.getUser().login(user1234);
        calleeApp.getUser().login(user4321);

        callerApp.getCall().makeCallFromKeypad("4321");

        callerApp.getCall().waitForOutgoingCall();
        calleeApp.getCall().waitForIncomingCall();

        Assert.assertTrue(
                callerApp.getCall().isCallActive()
        );

        Assert.assertTrue(
                calleeApp.getCall().isCallActive()
        );

        callerApp.getCall().pause(15000);

        callerApp.getCall().endCall();

        Assert.assertTrue(
                callerApp.getCall().waitUntilCallFinished()
        );

        Assert.assertTrue(
                calleeApp.getCall().waitUntilCallFinished()
        );
    }
}