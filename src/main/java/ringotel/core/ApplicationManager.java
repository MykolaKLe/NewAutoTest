package ringotel.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import ringotel.fw.CallHelper;
import ringotel.fw.ContactHelper;
import ringotel.fw.UserHelper;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ApplicationManager {

    private final String browser;

    private WebDriver driver;

    private UserHelper user;
    private ContactHelper contact;
    private CallHelper call;

    private static final String BASE_URL =
            "https://app.shell.ringotel.co/login";

    public ApplicationManager(String browser) {
        this.browser = browser;
    }

    public void init() {

        if (browser.equalsIgnoreCase("chrome")) {

            WebDriverManager.chromedriver().setup();

            Map<String, Object> prefs = new HashMap<>();

            prefs.put(
                    "profile.default_content_setting_values.media_stream_mic",
                    1
            );

            prefs.put(
                    "profile.default_content_setting_values.media_stream_camera",
                    1
            );

            prefs.put(
                    "profile.default_content_setting_values.notifications",
                    1
            );

            ChromeOptions options =
                    new ChromeOptions();

            options.setExperimentalOption(
                    "prefs",
                    prefs
            );

            options.addArguments(
                    "--use-fake-ui-for-media-stream"
            );

            driver =
                    new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("firefox")) {

            WebDriverManager.firefoxdriver().setup();

            FirefoxOptions options =
                    new FirefoxOptions();

            options.addPreference(
                    "media.navigator.permission.disabled",
                    true
            );

            options.addPreference(
                    "permissions.default.microphone",
                    1
            );

            options.addPreference(
                    "permissions.default.camera",
                    1
            );

            options.addPreference(
                    "permissions.default.desktop-notification",
                    1
            );

            driver =
                    new FirefoxDriver(options);

        } else if (browser.equalsIgnoreCase("edge")) {

            WebDriverManager.edgedriver().setup();

            Map<String, Object> prefs = new HashMap<>();

            prefs.put(
                    "profile.default_content_setting_values.media_stream_mic",
                    1
            );

            prefs.put(
                    "profile.default_content_setting_values.media_stream_camera",
                    1
            );

            prefs.put(
                    "profile.default_content_setting_values.notifications",
                    1
            );

            EdgeOptions options =
                    new EdgeOptions();

            options.setExperimentalOption(
                    "prefs",
                    prefs
            );

            options.addArguments(
                    "--use-fake-ui-for-media-stream"
            );

            driver =
                    new EdgeDriver(options);

        } else {

            throw new IllegalArgumentException(
                    "Unknown browser: "
                            + browser
            );
        }

        driver.manage()
                .window()
                .maximize();

        driver.manage()
                .timeouts()
                .implicitlyWait(
                        Duration.ofSeconds(0)
                );

        driver.get(BASE_URL);

        user =
                new UserHelper(driver);

        contact =
                new ContactHelper(driver);

        call =
                new CallHelper(driver);
    }

    public void stop() {

        if (driver != null) {

            driver.quit();

            driver = null;
        }
    }

    public UserHelper getUser() {
        return user;
    }

    public ContactHelper getContact() {
        return contact;
    }

    public CallHelper getCall() {
        return call;
    }

    public WebDriver getDriver() {
        return driver;
    }
}