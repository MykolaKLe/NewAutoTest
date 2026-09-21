package ringotel.core;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public final class TestEvidenceCollector {

    private static final DateTimeFormatter FOLDER_FORMAT =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd_HH-mm-ss-SSS"
            );

    private static final ThreadLocal<ActionInfo> LAST_ACTION =
            new ThreadLocal<>();

    private static final Object INDEX_LOCK =
            new Object();

    private TestEvidenceCollector() {
    }

    public static void rememberAction(
            String action,
            By locator
    ) {

        LAST_ACTION.set(
                new ActionInfo(
                        action,
                        locator
                )
        );
    }

    public static void clearContext() {

        LAST_ACTION.remove();
    }

    public static String capture(
            WebDriver driver,
            String testClass,
            String testMethod,
            Throwable throwable
    ) {

        if (driver == null) {
            return null;
        }

        try {

            Path runRoot =
                    resolveRunRoot();

            Files.createDirectories(
                    runRoot
            );

            String timestamp =
                    LocalDateTime
                            .now()
                            .format(
                                    FOLDER_FORMAT
                            );

            Path evidenceDirectory =
                    runRoot
                            .resolve(
                                    "evidence"
                            )
                            .resolve(
                                    sanitize(
                                            testClass
                                    )
                            )
                            .resolve(
                                    sanitize(
                                            testMethod
                                    )
                            )
                            .resolve(
                                    timestamp
                            );

            Files.createDirectories(
                    evidenceDirectory
            );

            ActionInfo actionInfo =
                    LAST_ACTION.get();

            WebElement element =
                    findEvidenceElement(
                            driver,
                            actionInfo
                    );

            Path screenshotPath =
                    evidenceDirectory.resolve(
                            "screenshot.png"
                    );

            saveScreenshot(
                    driver,
                    screenshotPath
            );

            String elementHtml = null;
            String contextHtml;
            Path highlightedScreenshotPath = null;

            ElementGeometry geometry = null;

            Boolean displayed = null;
            Boolean enabled = null;

            if (element != null) {

                try {

                    displayed =
                            element.isDisplayed();

                } catch (Exception ignored) {
                }

                try {

                    enabled =
                            element.isEnabled();

                } catch (Exception ignored) {
                }

                elementHtml =
                        getElementOuterHtml(
                                driver,
                                element
                        );

                contextHtml =
                        getContextHtml(
                                driver,
                                element
                        );

                geometry =
                        getElementGeometry(
                                driver,
                                element
                        );

                highlightedScreenshotPath =
                        evidenceDirectory.resolve(
                                "screenshot-highlighted.png"
                        );

                saveHighlightedScreenshot(
                        driver,
                        element,
                        highlightedScreenshotPath
                );

            } else {

                contextHtml =
                        getContextHtml(
                                driver,
                                null
                        );
            }

            Path elementHtmlPath = null;

            if (elementHtml != null
                    && !elementHtml.isBlank()) {

                elementHtmlPath =
                        evidenceDirectory.resolve(
                                "element.html"
                        );

                Files.writeString(
                        elementHtmlPath,
                        elementHtml,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
            }

            Path contextHtmlPath =
                    evidenceDirectory.resolve(
                            "context.html"
                    );

            Files.writeString(
                    contextHtmlPath,
                    contextHtml == null
                            ? ""
                            : contextHtml,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            Path stackTracePath =
                    evidenceDirectory.resolve(
                            "stacktrace.txt"
                    );

            String stackTrace =
                    getStackTrace(
                            throwable
                    );

            Files.writeString(
                    stackTracePath,
                    stackTrace,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            Path evidenceJsonPath =
                    evidenceDirectory.resolve(
                            "evidence.json"
                    );

            String json =
                    createEvidenceJson(
                            driver,
                            runRoot,
                            testClass,
                            testMethod,
                            timestamp,
                            actionInfo,
                            element != null,
                            displayed,
                            enabled,
                            geometry,
                            throwable,
                            screenshotPath,
                            highlightedScreenshotPath,
                            elementHtmlPath,
                            contextHtmlPath,
                            stackTracePath
                    );

            Files.writeString(
                    evidenceJsonPath,
                    json,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            appendEvidenceIndex(
                    runRoot,
                    evidenceJsonPath
            );

            return evidenceJsonPath
                    .toAbsolutePath()
                    .toString();

        } catch (Exception e) {

            return null;
        }
    }

    private static WebElement findEvidenceElement(
            WebDriver driver,
            ActionInfo actionInfo
    ) {

        if (actionInfo == null
                || actionInfo.locator == null) {

            return null;
        }

        try {

            List<WebElement> elements =
                    driver.findElements(
                            actionInfo.locator
                    );

            if (elements.isEmpty()) {
                return null;
            }

            for (WebElement element :
                    elements) {

                try {

                    if (element.isDisplayed()) {
                        return element;
                    }

                } catch (Exception ignored) {
                }
            }

            return elements.get(0);

        } catch (Exception e) {

            return null;
        }
    }

    private static void saveScreenshot(
            WebDriver driver,
            Path destination
    ) throws Exception {

        File temporary =
                ((TakesScreenshot) driver)
                        .getScreenshotAs(
                                OutputType.FILE
                        );

        Files.copy(
                temporary.toPath(),
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    private static void saveHighlightedScreenshot(
            WebDriver driver,
            WebElement element,
            Path destination
    ) {

        JavascriptExecutor javascriptExecutor =
                (JavascriptExecutor) driver;

        Object originalStyle = null;

        try {

            originalStyle =
                    javascriptExecutor.executeScript(
                            "return arguments[0].getAttribute('style');",
                            element
                    );

            javascriptExecutor.executeScript(
                    "arguments[0].style.outline='4px solid red';" +
                            "arguments[0].style.outlineOffset='3px';" +
                            "arguments[0].scrollIntoView({block:'center',inline:'center'});",
                    element
            );

            saveScreenshot(
                    driver,
                    destination
            );

        } catch (Exception ignored) {

        } finally {

            try {

                if (originalStyle == null) {

                    javascriptExecutor.executeScript(
                            "arguments[0].removeAttribute('style');",
                            element
                    );

                } else {

                    javascriptExecutor.executeScript(
                            "arguments[0].setAttribute('style', arguments[1]);",
                            element,
                            originalStyle
                    );
                }

            } catch (Exception ignored) {
            }
        }
    }

    private static String getElementOuterHtml(
            WebDriver driver,
            WebElement element
    ) {

        try {

            return String.valueOf(
                    ((JavascriptExecutor) driver)
                            .executeScript(
                                    "return arguments[0].outerHTML;",
                                    element
                            )
            );

        } catch (Exception e) {

            return null;
        }
    }

    private static String getContextHtml(
            WebDriver driver,
            WebElement element
    ) {

        try {

            JavascriptExecutor executor =
                    (JavascriptExecutor) driver;

            if (element != null) {

                Object result =
                        executor.executeScript(
                                "const el = arguments[0];" +
                                        "const context = el.closest(" +
                                        "'[role=\"dialog\"]," +
                                        "[data-call-overlay=\"frame\"]," +
                                        "article,section,main'" +
                                        ");" +
                                        "return context ? context.outerHTML : document.body.outerHTML;",
                                element
                        );

                return String.valueOf(
                        result
                );
            }

            Object result =
                    executor.executeScript(
                            "const main = document.querySelector('main');" +
                                    "return main ? main.outerHTML : document.body.outerHTML;"
                    );

            return String.valueOf(
                    result
            );

        } catch (Exception e) {

            try {

                return driver.getPageSource();

            } catch (Exception ignored) {

                return "";
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static ElementGeometry getElementGeometry(
            WebDriver driver,
            WebElement element
    ) {

        try {

            Map<String, Object> result =
                    (Map<String, Object>)
                            ((JavascriptExecutor) driver)
                                    .executeScript(
                                            "const r = arguments[0].getBoundingClientRect();" +
                                                    "return {" +
                                                    "x:r.x," +
                                                    "y:r.y," +
                                                    "width:r.width," +
                                                    "height:r.height," +
                                                    "pageX:r.x + window.scrollX," +
                                                    "pageY:r.y + window.scrollY" +
                                                    "};",
                                            element
                                    );

            return new ElementGeometry(
                    toDouble(
                            result.get(
                                    "x"
                            )
                    ),
                    toDouble(
                            result.get(
                                    "y"
                            )
                    ),
                    toDouble(
                            result.get(
                                    "width"
                            )
                    ),
                    toDouble(
                            result.get(
                                    "height"
                            )
                    ),
                    toDouble(
                            result.get(
                                    "pageX"
                            )
                    ),
                    toDouble(
                            result.get(
                                    "pageY"
                            )
                    )
            );

        } catch (Exception e) {

            return null;
        }
    }

    private static Double toDouble(
            Object value
    ) {

        if (value instanceof Number number) {

            return number.doubleValue();
        }

        return null;
    }

    private static String createEvidenceJson(
            WebDriver driver,
            Path runRoot,
            String testClass,
            String testMethod,
            String timestamp,
            ActionInfo actionInfo,
            boolean elementFound,
            Boolean displayed,
            Boolean enabled,
            ElementGeometry geometry,
            Throwable throwable,
            Path screenshot,
            Path highlightedScreenshot,
            Path elementHtml,
            Path contextHtml,
            Path stackTrace
    ) {

        StringBuilder json =
                new StringBuilder();

        json.append("{\n");

        appendJsonField(
                json,
                "timestamp",
                timestamp,
                true
        );

        appendJsonField(
                json,
                "testClass",
                testClass,
                true
        );

        appendJsonField(
                json,
                "testMethod",
                testMethod,
                true
        );

        appendJsonField(
                json,
                "browser",
                getBrowserName(
                        driver
                ),
                true
        );

        appendJsonField(
                json,
                "url",
                getCurrentUrl(
                        driver
                ),
                true
        );

        appendJsonField(
                json,
                "action",
                actionInfo == null
                        ? null
                        : actionInfo.action,
                true
        );

        appendJsonField(
                json,
                "locator",
                actionInfo == null
                        || actionInfo.locator == null
                        ? null
                        : actionInfo.locator.toString(),
                true
        );

        json.append(
                "  \"elementFound\": "
        );

        json.append(
                elementFound
        );

        json.append(
                ",\n"
        );

        appendJsonBooleanField(
                json,
                "displayed",
                displayed,
                true
        );

        appendJsonBooleanField(
                json,
                "enabled",
                enabled,
                true
        );

        appendJsonNumberField(
                json,
                "x",
                geometry == null
                        ? null
                        : geometry.x,
                true
        );

        appendJsonNumberField(
                json,
                "y",
                geometry == null
                        ? null
                        : geometry.y,
                true
        );

        appendJsonNumberField(
                json,
                "width",
                geometry == null
                        ? null
                        : geometry.width,
                true
        );

        appendJsonNumberField(
                json,
                "height",
                geometry == null
                        ? null
                        : geometry.height,
                true
        );

        appendJsonNumberField(
                json,
                "pageX",
                geometry == null
                        ? null
                        : geometry.pageX,
                true
        );

        appendJsonNumberField(
                json,
                "pageY",
                geometry == null
                        ? null
                        : geometry.pageY,
                true
        );

        appendJsonField(
                json,
                "errorType",
                throwable == null
                        ? null
                        : throwable
                          .getClass()
                          .getName(),
                true
        );

        appendJsonField(
                json,
                "error",
                throwable == null
                        ? null
                        : throwable
                          .getMessage(),
                true
        );

        appendJsonField(
                json,
                "screenshot",
                relativePath(
                        runRoot,
                        screenshot
                ),
                true
        );

        appendJsonField(
                json,
                "highlightedScreenshot",
                relativePath(
                        runRoot,
                        highlightedScreenshot
                ),
                true
        );

        appendJsonField(
                json,
                "elementHtml",
                relativePath(
                        runRoot,
                        elementHtml
                ),
                true
        );

        appendJsonField(
                json,
                "contextHtml",
                relativePath(
                        runRoot,
                        contextHtml
                ),
                true
        );

        appendJsonField(
                json,
                "stackTrace",
                relativePath(
                        runRoot,
                        stackTrace
                ),
                false
        );

        json.append(
                "}\n"
        );

        return json.toString();
    }

    private static void appendJsonField(
            StringBuilder json,
            String name,
            String value,
            boolean comma
    ) {

        json.append(
                "  \""
        );

        json.append(
                jsonEscape(
                        name
                )
        );

        json.append(
                "\": "
        );

        if (value == null) {

            json.append(
                    "null"
            );

        } else {

            json.append(
                    "\""
            );

            json.append(
                    jsonEscape(
                            value
                    )
            );

            json.append(
                    "\""
            );
        }

        if (comma) {

            json.append(
                    ","
            );
        }

        json.append(
                "\n"
        );
    }

    private static void appendJsonBooleanField(
            StringBuilder json,
            String name,
            Boolean value,
            boolean comma
    ) {

        json.append(
                "  \""
        );

        json.append(
                jsonEscape(
                        name
                )
        );

        json.append(
                "\": "
        );

        json.append(
                value == null
                        ? "null"
                        : value
                          .toString()
        );

        if (comma) {

            json.append(
                    ","
            );
        }

        json.append(
                "\n"
        );
    }

    private static void appendJsonNumberField(
            StringBuilder json,
            String name,
            Double value,
            boolean comma
    ) {

        json.append(
                "  \""
        );

        json.append(
                jsonEscape(
                        name
                )
        );

        json.append(
                "\": "
        );

        json.append(
                value == null
                        ? "null"
                        : value
                          .toString()
        );

        if (comma) {

            json.append(
                    ","
            );
        }

        json.append(
                "\n"
        );
    }

    private static void appendEvidenceIndex(
            Path runRoot,
            Path evidenceJson
    ) {

        synchronized (INDEX_LOCK) {

            try {

                Path index =
                        runRoot.resolve(
                                "evidence-index.jsonl"
                        );

                String entry =
                        "{\"evidence\":\""
                                + jsonEscape(
                                relativePath(
                                        runRoot,
                                        evidenceJson
                                )
                        )
                                + "\"}"
                                + System.lineSeparator();

                Files.writeString(
                        index,
                        entry,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                );

            } catch (Exception ignored) {
            }
        }
    }

    private static String getBrowserName(
            WebDriver driver
    ) {

        try {

            if (driver instanceof RemoteWebDriver remoteWebDriver) {

                return remoteWebDriver
                        .getCapabilities()
                        .getBrowserName();
            }

        } catch (Exception ignored) {
        }

        return null;
    }

    private static String getCurrentUrl(
            WebDriver driver
    ) {

        try {

            return driver.getCurrentUrl();

        } catch (Exception e) {

            return null;
        }
    }

    private static String getStackTrace(
            Throwable throwable
    ) {

        if (throwable == null) {
            return "";
        }

        StringWriter stringWriter =
                new StringWriter();

        PrintWriter printWriter =
                new PrintWriter(
                        stringWriter
                );

        throwable.printStackTrace(
                printWriter
        );

        printWriter.flush();

        return stringWriter.toString();
    }

    private static Path resolveRunRoot() {

        String environmentPath =
                System.getenv(
                        "RINGOTEL_RUN_DIR"
                );

        if (environmentPath != null
                && !environmentPath.isBlank()) {

            return Path.of(
                    environmentPath
            );
        }

        String propertyPath =
                System.getProperty(
                        "ringotel.run.dir"
                );

        if (propertyPath != null
                && !propertyPath.isBlank()) {

            return Path.of(
                    propertyPath
            );
        }

        return Path.of(
                "test_artifacts",
                LocalDateTime
                        .now()
                        .format(
                                FOLDER_FORMAT
                        )
        );
    }

    private static String relativePath(
            Path root,
            Path path
    ) {

        if (path == null) {
            return null;
        }

        try {

            return root
                    .toAbsolutePath()
                    .normalize()
                    .relativize(
                            path
                                    .toAbsolutePath()
                                    .normalize()
                    )
                    .toString()
                    .replace(
                            '\\',
                            '/'
                    );

        } catch (Exception e) {

            return path
                    .toString()
                    .replace(
                            '\\',
                            '/'
                    );
        }
    }

    private static String sanitize(
            String value
    ) {

        if (value == null
                || value.isBlank()) {

            return "unknown";
        }

        return value.replaceAll(
                "[^a-zA-Z0-9._-]",
                "_"
        );
    }

    private static String jsonEscape(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .replace(
                        "\\",
                        "\\\\"
                )
                .replace(
                        "\"",
                        "\\\""
                )
                .replace(
                        "\r",
                        "\\r"
                )
                .replace(
                        "\n",
                        "\\n"
                )
                .replace(
                        "\t",
                        "\\t"
                );
    }

    private static class ActionInfo {

        private final String action;

        private final By locator;

        private ActionInfo(
                String action,
                By locator
        ) {

            this.action =
                    action;

            this.locator =
                    locator;
        }
    }

    private static class ElementGeometry {

        private final Double x;
        private final Double y;
        private final Double width;
        private final Double height;
        private final Double pageX;
        private final Double pageY;

        private ElementGeometry(
                Double x,
                Double y,
                Double width,
                Double height,
                Double pageX,
                Double pageY
        ) {

            this.x =
                    x;

            this.y =
                    y;

            this.width =
                    width;

            this.height =
                    height;

            this.pageX =
                    pageX;

            this.pageY =
                    pageY;
        }
    }
}