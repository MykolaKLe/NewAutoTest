package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.model.User;
import ringotel.utils.MyDataProviders;

public class LoginTests extends TestBase {

    @Test(
            dataProvider = "ringotelUsers",
            dataProviderClass = MyDataProviders.class
    )
    public void loginPositiveTest(
            User user
    ) {

        String domain =
                user.getDomain();

        String extension =
                user.getUsername();

        logger.info(
                "Starting login test for {}",
                extension
        );

        logger.info(
                "Entering domain {}",
                domain
        );

        app.getUser()
                .enterDomain(
                        domain
                );

        logger.info(
                "Domain submitted successfully"
        );

        logger.info(
                "Entering credentials for {}",
                extension
        );

        app.getUser()
                .fillLoginForm(
                        user
                );

        logger.info(
                "Credentials entered for {}",
                extension
        );

        logger.info(
                "Clicking Log in button"
        );

        app.getUser()
                .clickOnLoginButton();

        app.getUser()
                .waitForLogin();

        logger.info(
                "Login completed for {}",
                extension
        );

        Assert.assertTrue(
                app.getUser()
                        .isLoggedIn(),
                "User "
                        + extension
                        + " was not logged in"
        );

        String screenshot =
                app.getUser()
                        .takeScreenShot();

        logger.info(
                "Login evidence -> {}",
                screenshot
        );

        logger.info(
                "Login scenario completed successfully for {}",
                extension
        );
    }
}