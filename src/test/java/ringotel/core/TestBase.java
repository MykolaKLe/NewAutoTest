package ringotel.core;

import org.openqa.selenium.remote.Browser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TestBase {

    protected static ApplicationManager app;

    protected final Logger logger =
            LoggerFactory.getLogger(getClass());


    @BeforeMethod
    public void setUpTest(Method method, Object[] parameters) {

        logger.info(
                "Start test {} with data: {}",
                method.getName(),
                Arrays.asList(parameters)
        );

        app = new ApplicationManager(
                System.getProperty(
                        "browser",
                        Browser.CHROME.browserName()
                )
        );

        app.init();
    }


    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {

        try {

            if (result.isSuccess()) {

                logger.info(
                        "PASSED: {}",
                        result.getMethod().getMethodName()
                );

            } else {

                String screenshot = null;

                try {

                    if (app != null && app.getUser() != null) {

                        screenshot =
                                app.getUser()
                                        .takeScreenShot();
                    }

                } catch (Exception e) {

                    logger.error(
                            "Could not create screenshot",
                            e
                    );
                }


                logger.error(
                        "FAILED: {}. Screenshot -> {}",
                        result.getMethod().getMethodName(),
                        screenshot
                );
            }

        } finally {


            if (app != null) {

                try {

                    app.stop();

                } catch (Exception e) {

                    logger.error(
                            "Could not stop browser",
                            e
                    );
                }
            }

            logger.info("Stop test");

            logger.info(
                    "******************************"
            );
        }
    }
}