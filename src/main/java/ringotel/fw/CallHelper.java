package ringotel.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ringotel.core.BaseHelper;

import java.time.Duration;
import java.util.List;

public class CallHelper extends BaseHelper {

    private final By keypadButton =
            By.cssSelector(
                    "button[aria-label='Keypad']"
            );

    private final By startCallButton =
            By.xpath(
                    "//button[.//*[name()='path' and contains(@d,'M12.4374 22.2159')]]"
            );

    private final By callFrame =
            By.cssSelector(
                    "[data-call-overlay='frame']"
            );

    private final By ringingCallFrame =
            By.cssSelector(
                    "[data-call-overlay='frame'][data-state='ringing']"
            );

    private final By activeCallFrame =
            By.cssSelector(
                    "[data-call-overlay='frame'][data-state='active']"
            );

    private final By answerCallButton =
            By.cssSelector(
                    "button[aria-label^='Answer call from ']"
            );

    private final By rejectIncomingCallButton =
            By.cssSelector(
                    "button[aria-label^='Reject call from ']"
            );

    private final By endCallButton =
            By.cssSelector(
                    "button[data-call-control='reject']"
            );

    private final By moreCallActionsButton =
            By.cssSelector(
                    "button[data-call-control='more']"
            );

    private final By transferButton =
            By.cssSelector(
                    "button[data-call-control='control']" +
                            "[aria-label^='Transfer call with ']"
            );

    private final By holdButton =
            By.cssSelector(
                    "button[data-call-control='control']" +
                            "[aria-label^='Hold call with ']"
            );

    private final By resumeButton =
            By.cssSelector(
                    "button[data-call-control='control']" +
                            "[aria-label^='Resume call with ']"
            );

    private final By muteButton =
            By.cssSelector(
                    "button[data-call-control='control']" +
                            "[aria-label^='Mute call with ']"
            );

    private final By unmuteButton =
            By.cssSelector(
                    "button[data-call-control='control']" +
                            "[aria-label^='Unmute call with ']"
            );

    private final By recordButton =
            By.cssSelector(
                    "button[data-call-control='control']" +
                            "[aria-label^='Record call with ']"
            );

    private final By stopRecordingButton =
            By.cssSelector(
                    "button[data-call-control='control']" +
                            "[aria-label^='Stop recording call with ']"
            );

    private final By separateWindowButton =
            By.cssSelector(
                    "button[aria-label='Open call in a separate window']"
            );

    private final By moreActionsPanel =
            By.cssSelector(
                    "[role='region'][aria-label^='More call actions for ']"
            );

    private final By videoCallButton =
            By.cssSelector(
                    "button[data-call-more-action='video']"
            );

    private final By deviceButton =
            By.cssSelector(
                    "button[data-call-more-action='device']"
            );

    private final By addPartyButton =
            By.cssSelector(
                    "button[data-call-more-action='add-party']"
            );

    private final By transcriptButton =
            By.cssSelector(
                    "button[data-call-more-action='transcript']"
            );

    private final By transferDialog =
            By.xpath(
                    "//div[@role='dialog' and @aria-modal='true']" +
                            "[.//h2[normalize-space()='Call Transfer']]"
            );

    private final By transferSearch =
            By.cssSelector(
                    "input[aria-label='Transfer To']"
            );

    private final By transferContactButtons =
            By.xpath(
                    "//div[@role='dialog' and @aria-modal='true']" +
                            "[.//h2[normalize-space()='Call Transfer']]" +
                            "//article/button"
            );

    private final By blindTransferButton =
            By.xpath(
                    "//div[@role='dialog' and @aria-modal='true']" +
                            "[.//h2[normalize-space()='Call Transfer']]" +
                            "//button[normalize-space()='Blind Transfer']"
            );

    private final By attendedTransferButton =
            By.xpath(
                    "//div[@role='dialog' and @aria-modal='true']" +
                            "[.//h2[normalize-space()='Call Transfer']]" +
                            "//button[normalize-space()='Attended Transfer']"
            );

    private final By closeTransferDialogButton =
            By.cssSelector(
                    "button[aria-label='Close transfer dialog']"
            );

    public CallHelper(WebDriver driver) {
        super(driver);
    }

    public void waitForClientReady() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        ).until(
                ExpectedConditions.elementToBeClickable(
                        keypadButton
                )
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        ).until(
                d -> !isVisible(
                        callFrame
                )
        );
    }

    public boolean isClientReady() {
        try {
            List<WebElement> buttons =
                    driver.findElements(
                            keypadButton
                    );

            if (buttons.isEmpty()) {
                return false;
            }

            WebElement button =
                    buttons.get(0);

            return button.isDisplayed()
                    && button.isEnabled()
                    && !isVisible(
                    callFrame
            );

        } catch (Exception e) {
            return false;
        }
    }

    public void openKeypad() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        ).until(
                ExpectedConditions.elementToBeClickable(
                        keypadButton
                )
        );

        click(
                keypadButton
        );
    }

    public void pressDigit(String digit) {
        click(
                By.cssSelector(
                        "button[aria-label='Dial key "
                                + digit
                                + "']"
                )
        );
    }

    public void pressPlus() {
        WebElement zero =
                waitForElementClickable(
                        By.cssSelector(
                                "button[aria-label='Dial key 0']"
                        )
                );

        new Actions(driver)
                .clickAndHold(zero)
                .pause(
                        Duration.ofMillis(
                                1200
                        )
                )
                .release()
                .perform();
    }

    public void dialNumber(String number) {
        for (char symbol :
                number.toCharArray()) {

            if (symbol == '+') {

                pressPlus();

            } else if (
                    Character.isDigit(
                            symbol
                    )
                            || symbol == '*'
                            || symbol == '#'
            ) {

                pressDigit(
                        String.valueOf(
                                symbol
                        )
                );

            } else {

                throw new IllegalArgumentException(
                        "Unsupported dial symbol: "
                                + symbol
                );
            }
        }
    }

    public void startCall() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.elementToBeClickable(
                        startCallButton
                )
        );

        click(
                startCallButton
        );
    }

    public void makeCallFromKeypad(String number) {
        waitForClientReady();
        openKeypad();
        dialNumber(
                number
        );
        startCall();
    }

    public boolean isNumberVisible(String number) {
        By numberLocator =
                By.xpath(
                        "//*[normalize-space()='"
                                + number
                                + "']"
                );

        return isElementPresent(
                numberLocator
        );
    }

    public void waitForNumberVisible(String number) {
        By numberLocator =
                By.xpath(
                        "//*[normalize-space()='"
                                + number
                                + "']"
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
                Duration.ofSeconds(35)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        ringingCallFrame
                )
        );
    }

    public boolean isIncomingCallVisible() {
        return isVisible(
                ringingCallFrame
        );
    }

    public void answerIncomingCall() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.elementToBeClickable(
                        answerCallButton
                )
        );

        click(
                answerCallButton
        );

        waitForActiveCall();
    }

    public void rejectIncomingCall() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.elementToBeClickable(
                        rejectIncomingCallButton
                )
        );

        click(
                rejectIncomingCallButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        ).until(
                d -> !isVisible(
                        ringingCallFrame
                )
        );
    }

    public void waitForOutgoingCall() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        ).until(
                d -> isVisible(
                        callFrame
                )
        );
    }

    public void waitForActiveCall() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        activeCallFrame
                )
        );
    }

    public boolean isCallActive() {
        return isVisible(
                activeCallFrame
        );
    }

    public boolean isCallOverlayVisible() {
        return isVisible(
                callFrame
        );
    }

    public boolean isSeparateWindowButtonAvailable() {
        return isVisible(
                separateWindowButton
        );
    }

    public void holdCall() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.elementToBeClickable(
                        holdButton
                )
        );

        click(
                holdButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        ).until(
                d -> isVisible(
                        resumeButton
                )
        );
    }

    public void resumeCall() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.elementToBeClickable(
                        resumeButton
                )
        );

        click(
                resumeButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        ).until(
                d -> isVisible(
                        holdButton
                )
        );
    }

    public boolean isCallOnHold() {
        return isVisible(
                resumeButton
        );
    }

    public void muteCall() {
        click(
                muteButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        unmuteButton
                )
        );
    }

    public void unmuteCall() {
        click(
                unmuteButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        muteButton
                )
        );
    }

    public boolean isMuted() {
        return isVisible(
                unmuteButton
        );
    }

    public void startRecording() {
        click(
                recordButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        stopRecordingButton
                )
        );
    }

    public void stopRecording() {
        click(
                stopRecordingButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        recordButton
                )
        );
    }

    public boolean isRecording() {
        return isVisible(
                stopRecordingButton
        );
    }

    public void openMoreCallActions() {
        click(
                moreCallActionsButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        moreActionsPanel
                )
        );
    }

    public void closeMoreCallActions() {
        if (!isVisible(
                moreActionsPanel
        )) {
            return;
        }

        click(
                moreCallActionsButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                d -> !isVisible(
                        moreActionsPanel
                )
        );
    }

    public boolean isMoreActionsOpen() {
        return isVisible(
                moreActionsPanel
        );
    }

    public boolean isVideoActionAvailable() {
        return isVisible(
                videoCallButton
        );
    }

    public boolean isDeviceActionAvailable() {
        return isVisible(
                deviceButton
        );
    }

    public boolean isAddPartyActionAvailable() {
        return isVisible(
                addPartyButton
        );
    }

    public boolean isTranscriptActionAvailable() {
        return isVisible(
                transcriptButton
        );
    }

    public void openTransferDialog() {
        click(
                transferButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        transferDialog
                )
        );
    }

    public boolean isTransferDialogOpen() {
        return isVisible(
                transferDialog
        );
    }

    public boolean isTransferSearchAvailable() {
        return isVisible(
                transferSearch
        );
    }

    public void selectFirstTransferContact() {
        WebElement contact =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(20)
                ).until(
                        d -> {

                            List<WebElement> contacts =
                                    d.findElements(
                                            transferContactButtons
                                    );

                            for (WebElement element :
                                    contacts) {

                                try {

                                    if (
                                            element.isDisplayed()
                                                    && element.isEnabled()
                                    ) {

                                        return element;
                                    }

                                } catch (Exception ignored) {
                                }
                            }

                            return null;
                        }
                );

        contact.click();

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                d -> isBlindTransferEnabled()
                        && isAttendedTransferEnabled()
        );
    }

    public boolean isBlindTransferButtonAvailable() {
        return isVisible(
                blindTransferButton
        );
    }

    public boolean isAttendedTransferButtonAvailable() {
        return isVisible(
                attendedTransferButton
        );
    }

    public boolean isBlindTransferEnabled() {
        List<WebElement> buttons =
                driver.findElements(
                        blindTransferButton
                );

        if (buttons.isEmpty()) {
            return false;
        }

        WebElement button =
                buttons.get(0);

        return button.isDisplayed()
                && button.isEnabled()
                && !"true".equals(
                button.getAttribute(
                        "data-disabled"
                )
        );
    }

    public boolean isAttendedTransferEnabled() {
        List<WebElement> buttons =
                driver.findElements(
                        attendedTransferButton
                );

        if (buttons.isEmpty()) {
            return false;
        }

        WebElement button =
                buttons.get(0);

        return button.isDisplayed()
                && button.isEnabled()
                && !"true".equals(
                button.getAttribute(
                        "data-disabled"
                )
        );
    }

    public void closeTransferDialog() {
        click(
                closeTransferDialogButton
        );

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                d -> !isVisible(
                        transferDialog
                )
        );
    }

    public void endCall() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        ).until(
                ExpectedConditions.elementToBeClickable(
                        endCallButton
                )
        );

        click(
                endCallButton
        );
    }

    public void waitForCallFinished() {
        new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        ).until(
                d -> !isVisible(
                        callFrame
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

    public void waitForIdleAfterCall() {
        waitForCallFinished();

        new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        ).until(
                d -> isVisible(
                        keypadButton
                )
        );
    }

    public void waitUpToAndFinishCall(int seconds) {
        try {

            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(20)
            ).until(
                    d -> isVisible(
                            callFrame
                    )
            );

        } catch (TimeoutException e) {

            return;
        }

        long deadline =
                System.currentTimeMillis()
                        + seconds * 1000L;

        while (
                System.currentTimeMillis()
                        < deadline
        ) {

            if (!isVisible(
                    callFrame
            )) {

                return;
            }

            pause(
                    500
            );
        }

        if (
                isVisible(
                        callFrame
                )
                        && isVisible(
                        endCallButton
                )
        ) {

            endCall();

            waitForCallFinished();
        }
    }

    private boolean isVisible(By locator) {
        List<WebElement> elements =
                driver.findElements(
                        locator
                );

        for (WebElement element :
                elements) {

            try {

                if (element.isDisplayed()) {
                    return true;
                }

            } catch (Exception ignored) {
            }
        }

        return false;
    }
}