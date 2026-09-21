package ringotel.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ringotel.core.BaseHelper;
import ringotel.model.User;

public class UserHelper extends BaseHelper {

    private final By domainInput =
            By.cssSelector(
                    "input[placeholder='Company domain']"
            );

    private final By domainContinueButton =
            By.xpath(
                    "//input[@placeholder='Company domain']" +
                            "/following::button[1]"
            );

    private final By usernameInput =
            By.cssSelector(
                    "input[placeholder='Enter username']"
            );

    private final By passwordInput =
            By.cssSelector(
                    "input[placeholder='Enter password']"
            );

    private final By loginButton =
            By.xpath(
                    "//button[normalize-space()='Log in']"
            );

    private final By accountButton =
            By.cssSelector(
                    "button[aria-label='Account']"
            );

    public UserHelper(
            WebDriver driver
    ) {

        super(
                driver
        );
    }

    public void enterDomain(
            String domain
    ) {

        type(
                domainInput,
                domain
        );

        click(
                domainContinueButton
        );

        waitForElement(
                usernameInput
        );
    }

    public void fillLoginForm(
            User user
    ) {

        type(
                usernameInput,
                user.getUsername()
        );

        type(
                passwordInput,
                user.getPassword()
        );
    }

    public void clickOnLoginButton() {

        click(
                loginButton
        );
    }

    public void waitForLogin() {

        waitForElement(
                accountButton
        );
    }

    public void login(
            User user
    ) {

        enterDomain(
                user.getDomain()
        );

        fillLoginForm(
                user
        );

        clickOnLoginButton();

        waitForLogin();
    }

    public boolean isLoggedIn() {

        return isElementPresent(
                accountButton
        )
                || driver
                .getCurrentUrl()
                .contains(
                        "/inbox/"
                );
    }
}