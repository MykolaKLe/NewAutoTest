package ringotel.core;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class BaseHelper {

    protected WebDriver driver;

    protected WebDriverWait wait;

    public BaseHelper(
            WebDriver driver
    ) {

        this.driver =
                driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(
                                15
                        )
                );
    }

    public void click(
            By locator
    ) {

        markEvidenceAction(
                "click",
                locator
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        locator
                )
        ).click();
    }

    public void type(
            By locator,
            String text
    ) {

        markEvidenceAction(
                "type",
                locator
        );

        WebElement element =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                locator
                        )
                );

        element.click();

        element.clear();

        element.sendKeys(
                text
        );
    }

    public boolean isElementPresent(
            By locator
    ) {

        return driver
                .findElements(
                        locator
                )
                .size() > 0;
    }

    public WebElement waitForElement(
            By locator
    ) {

        markEvidenceAction(
                "waitForElement",
                locator
        );

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        locator
                )
        );
    }

    public WebElement waitForElementClickable(
            By locator
    ) {

        markEvidenceAction(
                "waitForElementClickable",
                locator
        );

        return wait.until(
                ExpectedConditions.elementToBeClickable(
                        locator
                )
        );
    }

    protected void markEvidenceAction(
            String action,
            By locator
    ) {

        TestEvidenceCollector
                .rememberAction(
                        action,
                        locator
                );
    }

    public static void clearEvidenceContext() {

        TestEvidenceCollector
                .clearContext();
    }

    public String captureFailureEvidence(
            String testClass,
            String testMethod,
            Throwable throwable
    ) {

        return TestEvidenceCollector
                .capture(
                        driver,
                        testClass,
                        testMethod,
                        throwable
                );
    }

    public void pause(
            int millis
    ) {

        try {

            Thread.sleep(
                    millis
            );

        } catch (InterruptedException e) {

            Thread.currentThread()
                    .interrupt();

            throw new RuntimeException(
                    "Pause was interrupted",
                    e
            );
        }
    }

    public String takeScreenShot() {

        Path screenshotsDirectory =
                resolveScreenshotsDirectory();

        try {

            Files.createDirectories(
                    screenshotsDirectory
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to create screenshots directory",
                    e
            );
        }

        File temporaryScreenshot =
                ((TakesScreenshot) driver)
                        .getScreenshotAs(
                                OutputType.FILE
                        );

        Path screenshot =
                screenshotsDirectory.resolve(
                        "screen-"
                                + System.currentTimeMillis()
                                + "-"
                                + Thread.currentThread()
                                .threadId()
                                + ".png"
                );

        try {

            Files.copy(
                    temporaryScreenshot.toPath(),
                    screenshot,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to save screenshot",
                    e
            );
        }

        return screenshot
                .toAbsolutePath()
                .toString();
    }

    private Path resolveScreenshotsDirectory() {

        String runDirectory =
                System.getenv(
                        "RINGOTEL_RUN_DIR"
                );

        if (runDirectory != null
                && !runDirectory.isBlank()) {

            return Path.of(
                    runDirectory,
                    "screenshots"
            );
        }

        return Path.of(
                "screenshots"
        );
    }
}