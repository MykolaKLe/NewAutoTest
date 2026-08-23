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
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class BaseHelper {

    protected WebDriver driver;

    protected WebDriverWait wait;


    public BaseHelper(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );
    }


    public void click(By locator) {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        locator
                )
        ).click();
    }


    public void type(By locator, String text) {

        WebElement element =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                locator
                        )
                );

        element.click();
        element.clear();
        element.sendKeys(text);
    }


    public boolean isElementPresent(By locator) {

        return driver
                .findElements(locator)
                .size() > 0;
    }


    public WebElement waitForElement(By locator) {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        locator
                )
        );
    }


    public WebElement waitForElementClickable(By locator) {

        return wait.until(
                ExpectedConditions.elementToBeClickable(
                        locator
                )
        );
    }


    public void pause(int millis) {

        try {

            Thread.sleep(millis);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Pause was interrupted",
                    e
            );
        }
    }


    public String takeScreenShot() {

        File screenshotsDir =
                new File("screenshots");

        if (!screenshotsDir.exists()) {

            screenshotsDir.mkdirs();
        }


        File tempScreenshot =
                ((TakesScreenshot) driver)
                        .getScreenshotAs(
                                OutputType.FILE
                        );


        File screenshot =
                new File(
                        screenshotsDir,
                        "screen-"
                                + System.currentTimeMillis()
                                + ".png"
                );


        try {

            Files.copy(
                    tempScreenshot.toPath(),
                    screenshot.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to save screenshot",
                    e
            );
        }


        return screenshot.getAbsolutePath();
    }
}