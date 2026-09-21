package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.core.TestData;
import ringotel.model.User;

public class PresenceTests extends TestBase {

    @Test
    public void changePresenceStatuses() {

        User user =
                TestData.getUser1();

        String extension =
                user.getUsername();

        logger.info(
                "Logging in {}",
                extension
        );

        app.getUser()
                .login(
                        user
                );

        logger.info(
                "{} logged in successfully",
                extension
        );

        logger.info(
                "{}: clicking Busy",
                extension
        );

        app.getPresence()
                .setBusy();

        Assert.assertTrue(
                app.getPresence()
                        .isBusy(),
                "Busy status was not set for "
                        + extension
        );

        logger.info(
                "{}: Busy status confirmed",
                extension
        );

        String busyScreenshot =
                app.getPresence()
                        .takeScreenShot();

        logger.info(
                "Busy evidence -> {}",
                busyScreenshot
        );

        app.getPresence()
                .pause(
                        1500
                );

        logger.info(
                "{}: clicking At Desk",
                extension
        );

        app.getPresence()
                .setAtDesk();

        Assert.assertTrue(
                app.getPresence()
                        .isAtDesk(),
                "At Desk status was not set for "
                        + extension
        );

        logger.info(
                "{}: At Desk status confirmed",
                extension
        );

        String atDeskScreenshot =
                app.getPresence()
                        .takeScreenShot();

        logger.info(
                "At Desk evidence -> {}",
                atDeskScreenshot
        );

        app.getPresence()
                .pause(
                        1500
                );

        logger.info(
                "{}: clicking Online",
                extension
        );

        app.getPresence()
                .setOnline();

        Assert.assertTrue(
                app.getPresence()
                        .isOnline(),
                "Online status was not set for "
                        + extension
        );

        logger.info(
                "{}: Online status confirmed",
                extension
        );

        String onlineScreenshot =
                app.getPresence()
                        .takeScreenShot();

        logger.info(
                "Online evidence -> {}",
                onlineScreenshot
        );

        app.getPresence()
                .pause(
                        1500
                );

        logger.info(
                "Presence scenario completed successfully for {}",
                extension
        );
    }
}