package ringotel.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ringotel.core.BaseHelper;

import java.time.Duration;

public class CallHelper extends BaseHelper {

    private final By keypadButton =
            By.cssSelector("button[aria-label='Keypad']");

    private final By startCallButton =
            By.xpath(
                    "//button[.//*[name()='path' and contains(@d,'M12.4374 22.2159')]]"
            );

    private final By endCallButton =
            By.xpath(
                    "//button[.//*[name()='path' and contains(@d,'M11.251 5.87385')]]"
            );

    public CallHelper(WebDriver driver) {
        super(driver);
    }

    public void openKeypad() {
        click(keypadButton);
    }

    public void pressDigit(String digit) {
        click(
                By.cssSelector(
                        "button[aria-label='Dial key " + digit + "']"
                )
        );
    }

    public void dialNumber(String number) {

        for (char digit : number.toCharArray()) {
            pressDigit(String.valueOf(digit));
        }
    }

    public void startCall() {
        click(startCallButton);
    }

    public void makeCallFromKeypad(String number) {

        openKeypad();
        dialNumber(number);
        startCall();
    }

    public boolean isNumberVisible(String number) {

        By numberLocator =
                By.xpath(
                        "//*[normalize-space()='" + number + "']"
                );

        return isElementPresent(numberLocator);
    }

    public void waitForNumberVisible(String number) {

        By numberLocator =
                By.xpath(
                        "//*[normalize-space()='" + number + "']"
                );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        numberLocator
                )
        );
    }

    public void waitForIncomingCall() {

        new WebDriverWait(
                driver,
                Duration.ofSeconds(25)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        endCallButton
                )
        );
    }

    public void waitForOutgoingCall() {

        new WebDriverWait(
                driver,
                Duration.ofSeconds(25)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        endCallButton
                )
        );
    }

    public boolean isCallActive() {

        return isElementPresent(endCallButton);
    }

    public void endCall() {
        click(endCallButton);
    }

    public void waitForCallFinished() {

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.invisibilityOfElementLocated(
                        endCallButton
                )
        );
    }

    public boolean waitUntilCallFinished() {

        try {

            waitForCallFinished();
            return true;

        } catch (TimeoutException e) {

            return false;
        }
    }
}