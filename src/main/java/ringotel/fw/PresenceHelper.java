package ringotel.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ringotel.core.BaseHelper;

import java.util.List;

public class PresenceHelper extends BaseHelper {

    private final By accountButton =
            By.cssSelector("button[aria-label='Account']");

    private final By onlineStatus =
            By.cssSelector(
                    "button[data-item-id='user-presence-online']"
            );

    private final By busyStatus =
            By.cssSelector(
                    "button[data-item-id='user-presence-dnd']"
            );

    private final By atDeskStatus =
            By.cssSelector(
                    "button[data-item-id='user-presence-at-desk']"
            );

    public PresenceHelper(WebDriver driver) {
        super(driver);
    }

    public PresenceHelper openPresenceMenu() {

        if (!isPresenceMenuOpen()) {

            click(accountButton);

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            onlineStatus
                    )
            );
        }

        return this;
    }

    public boolean isPresenceMenuOpen() {

        List<WebElement> elements =
                driver.findElements(onlineStatus);

        for (WebElement element : elements) {

            if (element.isDisplayed()) {
                return true;
            }
        }

        return false;
    }

    public PresenceHelper setOnline() {

        openPresenceMenu();

        click(onlineStatus);

        waitForStatus(
                "user-presence-online",
                "online"
        );

        return this;
    }

    public PresenceHelper setBusy() {

        openPresenceMenu();

        click(busyStatus);

        waitForStatus(
                "user-presence-dnd",
                "busy"
        );

        return this;
    }

    public PresenceHelper setAtDesk() {

        openPresenceMenu();

        click(atDeskStatus);

        waitForStatus(
                "user-presence-at-desk",
                "at-the-desk"
        );

        return this;
    }

    public boolean isOnline() {

        return isStatus(
                "user-presence-online",
                "online"
        );
    }

    public boolean isBusy() {

        return isStatus(
                "user-presence-dnd",
                "busy"
        );
    }

    public boolean isAtDesk() {

        return isStatus(
                "user-presence-at-desk",
                "at-the-desk"
        );
    }

    private boolean isStatus(
            String itemId,
            String status
    ) {

        By selectedMenuStatus =
                By.cssSelector(
                        "button[data-item-id='"
                                + itemId
                                + "'][aria-checked='true']"
                );

        By accountStatus =
                By.cssSelector(
                        "button[aria-label='Account'] "
                                + "[data-status='"
                                + status
                                + "']"
                );

        return isVisible(selectedMenuStatus)
                || isVisible(accountStatus);
    }

    private void waitForStatus(
            String itemId,
            String status
    ) {

        wait.until(driver ->
                isStatus(itemId, status)
        );
    }

    private boolean isVisible(By locator) {

        List<WebElement> elements =
                driver.findElements(locator);

        for (WebElement element : elements) {

            if (element.isDisplayed()) {
                return true;
            }
        }

        return false;
    }
}