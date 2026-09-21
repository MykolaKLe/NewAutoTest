package ringotel.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ringotel.core.BaseHelper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatHelper extends BaseHelper {

    private final By contactsButton =
            By.cssSelector(
                    "button[aria-label='Contacts']"
            );

    private final By contactsTab =
            By.xpath(
                    "//button[.//span[normalize-space()='Contacts']]"
            );

    private final By contactsSearch =
            By.cssSelector(
                    "input[aria-label='Search']"
            );

    private final By openCreateMenuButton =
            By.cssSelector(
                    "button[aria-label='Open create menu']"
            );

    private final By newMessageButton =
            By.cssSelector(
                    "button[data-item-id='message']"
            );

    private final By newMessageSearch =
            By.cssSelector(
                    "input[aria-label='Search contacts']"
            );

    private final By messageInput =
            By.cssSelector(
                    "textarea[name='chat-composer-message']"
            );

    private final By newMessageContactRows =
            By.cssSelector(
                    "div[data-row-shell='true'][data-interactive='true'] > button"
            );

    private final By chatRows =
            By.xpath(
                    "//button[@aria-pressed and ./div[@data-id]]"
            );

    private final By activeChat =
            By.xpath(
                    "//button[@aria-pressed='true' and ./div[@data-id]]"
            );

    private final By deleteChatButton =
            By.cssSelector(
                    "button[data-item-id='delete-chat']"
            );

    private final By possibleChatMenuButtons =
            By.cssSelector(
                    "main button[aria-haspopup='menu'], " +
                            "main button[aria-expanded]"
            );

    private final By deleteChatDialog =
            By.cssSelector(
                    "div[role='dialog'][aria-modal='true']"
            );

    private final By confirmDeleteChatButton =
            By.xpath(
                    "//div[@role='dialog' and @aria-modal='true']" +
                            "//footer/button[last()]"
            );

    public ChatHelper(WebDriver driver) {
        super(driver);
    }

    public ChatHelper openContacts() {

        click(
                contactsButton
        );

        return this;
    }

    public ChatHelper openContactsTab() {

        click(
                contactsTab
        );

        return this;
    }

    public ChatHelper searchContact(
            String name
    ) {

        setReactInputValue(
                contactsSearch,
                name
        );

        return this;
    }

    public ChatHelper openContact(
            String name
    ) {

        By contact =
                By.xpath(
                        "//*[normalize-space()="
                                + xpathLiteral(
                                normalizeContactName(
                                        name
                                )
                        )
                                + "]"
                );

        click(
                contact
        );

        waitForElement(
                messageInput
        );

        return this;
    }

    public ChatHelper openContactFromContacts(
            String name
    ) {

        openContacts();

        openContactsTab();

        searchContact(
                name
        );

        openContact(
                name
        );

        return this;
    }

    public ChatHelper openNewMessage() {

        if (isNewMessageWindowOpen()) {

            clearNewMessageSearch();

            return this;
        }

        click(
                openCreateMenuButton
        );

        click(
                newMessageButton
        );

        waitForElement(
                newMessageSearch
        );

        clearNewMessageSearch();

        return this;
    }

    public boolean isNewMessageWindowOpen() {

        return isVisible(
                newMessageSearch
        );
    }

    public ChatHelper ensureNewMessageWindowOpen() {

        if (!isNewMessageWindowOpen()) {

            openNewMessage();

        } else {

            clearNewMessageSearch();
        }

        return this;
    }

    public ChatHelper clearNewMessageSearch() {

        if (!isVisible(
                newMessageSearch
        )) {

            return this;
        }

        setReactInputValue(
                newMessageSearch,
                ""
        );

        return this;
    }

    public ChatHelper searchNewMessageContact(
            String name
    ) {

        setReactInputValue(
                newMessageSearch,
                name
        );

        String expectedName =
                normalizeContactName(
                        name
                );

        wait.until(
                driver -> {

                    List<WebElement> rows =
                            driver.findElements(
                                    newMessageContactRows
                            );

                    for (WebElement row : rows) {

                        try {

                            if (!row.isDisplayed()) {
                                continue;
                            }

                            String rowName =
                                    extractContactName(
                                            row
                                    );

                            if (expectedName.equals(
                                    rowName
                            )) {

                                return true;
                            }

                        } catch (Exception ignored) {
                        }
                    }

                    return false;
                }
        );

        return this;
    }

    public ChatHelper selectNewMessageContact(
            String name
    ) {

        return selectNewMessageContact(
                name,
                1
        );
    }

    public ChatHelper selectNewMessageContact(
            String name,
            int occurrence
    ) {

        WebElement contact =
                waitForNewMessageContact(
                        name,
                        occurrence
                );

        contact.click();

        pause(
                500
        );

        return this;
    }

    public ChatHelper openContactFromNewMessage(
            String name
    ) {

        return openContactFromNewMessage(
                name,
                1
        );
    }

    public ChatHelper openContactFromNewMessage(
            String name,
            int occurrence
    ) {

        openNewMessage();

        searchNewMessageContact(
                name
        );

        selectNewMessageContact(
                name,
                occurrence
        );

        return this;
    }

    public String openRandomAvailableContact(
            int messageInputWaitSeconds
    ) {

        openNewMessage();

        List<RecipientCandidate> candidates =
                getNewMessageContactCandidates();

        if (candidates.isEmpty()) {

            throw new RuntimeException(
                    "No contacts were found in New Message"
            );
        }

        Collections.shuffle(
                candidates
        );

        for (RecipientCandidate candidate : candidates) {

            try {

                ensureNewMessageWindowOpen();

                searchNewMessageContact(
                        candidate.name
                );

                selectNewMessageContact(
                        candidate.name,
                        candidate.occurrence
                );

                if (isMessageInputAvailable(
                        messageInputWaitSeconds
                )) {

                    return candidate.name;
                }

            } catch (Exception ignored) {
            }
        }

        throw new RuntimeException(
                "No contact with available messaging was found"
        );
    }

    public boolean isMessageInputAvailable(
            int seconds
    ) {

        try {

            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(
                            seconds
                    )
            ).until(
                    ExpectedConditions.visibilityOfElementLocated(
                            messageInput
                    )
            );

            return true;

        } catch (TimeoutException e) {

            return false;
        }
    }

    public ChatHelper sendMessage(
            String message
    ) {

        WebElement input =
                waitForElement(
                        messageInput
                );

        input.click();

        input.sendKeys(
                message
        );

        input.sendKeys(
                Keys.ENTER
        );

        return this;
    }

    public ChatHelper verifyMessage(
            String message
    ) {

        By messageLocator =
                By.xpath(
                        "//main//*[" +
                                "normalize-space()="
                                + xpathLiteral(
                                normalizeContactName(
                                        message
                                )
                        )
                                + "]"
                );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        messageLocator
                )
        );

        return this;
    }

    public List<String> getNewMessageContactNames() {

        List<RecipientCandidate> candidates =
                getNewMessageContactCandidates();

        List<String> contacts =
                new ArrayList<>();

        for (RecipientCandidate candidate : candidates) {

            contacts.add(
                    candidate.name
            );
        }

        return contacts;
    }

    public int getChatCount() {

        return driver.findElements(
                chatRows
        ).size();
    }

    public boolean hasChats() {

        return getChatCount() > 0;
    }

    public ChatHelper openFirstChat() {

        WebElement chat =
                wait.until(
                        driver -> {

                            List<WebElement> chats =
                                    driver.findElements(
                                            chatRows
                                    );

                            for (WebElement element : chats) {

                                try {

                                    if (element.isDisplayed()) {
                                        return element;
                                    }

                                } catch (Exception ignored) {
                                }
                            }

                            return null;
                        }
                );

        String chatId =
                chat.findElement(
                                By.cssSelector(
                                        "div[data-id]"
                                )
                        )
                        .getAttribute(
                                "data-id"
                        );

        chat.click();

        By selectedChat =
                By.xpath(
                        "//button[@aria-pressed='true' " +
                                "and ./div[@data-id="
                                + xpathLiteral(
                                chatId
                        )
                                + "]]"
                );

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        selectedChat
                )
        );

        pause(
                300
        );

        return this;
    }

    public String getActiveChatId() {

        WebElement active =
                waitForElement(
                        activeChat
                );

        return active
                .findElement(
                        By.cssSelector(
                                "div[data-id]"
                        )
                )
                .getAttribute(
                        "data-id"
                );
    }

    public String getActiveChatName() {

        WebElement active =
                waitForElement(
                        activeChat
                );

        List<WebElement> spans =
                active.findElements(
                        By.xpath(
                                ".//span[normalize-space()]"
                        )
                );

        for (WebElement span : spans) {

            String text =
                    normalizeContactName(
                            span.getText()
                    );

            if (text.isEmpty()) {
                continue;
            }

            if (text.matches(
                    "\\d{1,2}:\\d{2}.*"
            )) {
                continue;
            }

            if (text.length() <= 2) {
                continue;
            }

            return text;
        }

        return getActiveChatId();
    }

    public ChatHelper openCurrentChatMenu() {

        if (isVisible(
                deleteChatButton
        )) {

            return this;
        }

        List<WebElement> buttons =
                driver.findElements(
                        possibleChatMenuButtons
                );

        for (WebElement button : buttons) {

            try {

                if (!button.isDisplayed()
                        || !button.isEnabled()) {

                    continue;
                }

                button.click();

                pause(
                        250
                );

                if (isVisible(
                        deleteChatButton
                )) {

                    return this;
                }

                if ("true".equals(
                        button.getAttribute(
                                "aria-expanded"
                        )
                )) {

                    button.click();

                    pause(
                            150
                    );
                }

            } catch (Exception ignored) {
            }
        }

        throw new RuntimeException(
                "Current chat options menu was not found"
        );
    }

    public ChatHelper deleteCurrentChat() {

        String chatId =
                getActiveChatId();

        openCurrentChatMenu();

        click(
                deleteChatButton
        );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        deleteChatDialog
                )
        );

        WebElement confirmButton =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                confirmDeleteChatButton
                        )
                );

        confirmButton.click();

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        deleteChatDialog
                )
        );

        try {

            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(10)
            ).until(
                    driver -> !isChatPresent(
                            chatId
                    )
            );

        } catch (TimeoutException e) {

            throw new RuntimeException(
                    "Chat was not deleted. Chat id: "
                            + chatId
            );
        }

        pause(
                400
        );

        return this;
    }

    public boolean isChatPresent(
            String chatId
    ) {

        By chat =
                By.xpath(
                        "//button[@aria-pressed " +
                                "and ./div[@data-id="
                                + xpathLiteral(
                                chatId
                        )
                                + "]]"
                );

        return !driver.findElements(
                chat
        ).isEmpty();
    }

    private WebElement waitForNewMessageContact(
            String name,
            int occurrence
    ) {

        String expectedName =
                normalizeContactName(
                        name
                );

        return new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        ).until(
                driver -> {

                    List<WebElement> rows =
                            driver.findElements(
                                    newMessageContactRows
                            );

                    int currentOccurrence =
                            0;

                    for (WebElement row : rows) {

                        try {

                            if (!row.isDisplayed()) {
                                continue;
                            }

                            String rowName =
                                    extractContactName(
                                            row
                                    );

                            if (!expectedName.equals(
                                    rowName
                            )) {

                                continue;
                            }

                            currentOccurrence++;

                            if (currentOccurrence
                                    == occurrence) {

                                return row;
                            }

                        } catch (Exception ignored) {
                        }
                    }

                    return null;
                }
        );
    }

    private List<RecipientCandidate> getNewMessageContactCandidates() {

        waitForElement(
                newMessageSearch
        );

        clearNewMessageSearch();

        wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        newMessageContactRows
                )
        );

        List<WebElement> rows =
                driver.findElements(
                        newMessageContactRows
                );

        List<RecipientCandidate> contacts =
                new ArrayList<>();

        Map<String, Integer> occurrences =
                new HashMap<>();

        for (WebElement row : rows) {

            try {

                if (!row.isDisplayed()) {
                    continue;
                }

                String name =
                        extractContactName(
                                row
                        );

                if (name.isEmpty()) {
                    continue;
                }

                int occurrence =
                        occurrences.merge(
                                name,
                                1,
                                Integer::sum
                        );

                contacts.add(
                        new RecipientCandidate(
                                name,
                                occurrence
                        )
                );

            } catch (Exception ignored) {
            }
        }

        return contacts;
    }

    private String extractContactName(
            WebElement row
    ) {

        List<WebElement> textElements =
                row.findElements(
                        By.xpath(
                                ".//span[normalize-space()]"
                        )
                );

        for (int i =
             textElements.size() - 1;
             i >= 0;
             i--) {

            String text =
                    normalizeContactName(
                            textElements
                                    .get(i)
                                    .getText()
                    );

            if (!text.isEmpty()) {
                return text;
            }
        }

        return "";
    }

    private String normalizeContactName(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .replace(
                        "\uFFFC",
                        ""
                )
                .replace(
                        '\u00A0',
                        ' '
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }

    private void setReactInputValue(
            By locator,
            String value
    ) {

        WebElement input =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                locator
                        )
                );

        JavascriptExecutor javascriptExecutor =
                (JavascriptExecutor) driver;

        javascriptExecutor.executeScript(
                "const input = arguments[0];" +
                        "const value = arguments[1];" +
                        "const setter = Object.getOwnPropertyDescriptor(" +
                        "window.HTMLInputElement.prototype, 'value').set;" +
                        "setter.call(input, value);" +
                        "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "input.dispatchEvent(new Event('change', { bubbles: true }));",
                input,
                value
        );

        wait.until(
                driver -> {

                    WebElement currentInput =
                            driver.findElement(
                                    locator
                            );

                    String currentValue =
                            currentInput.getDomProperty(
                                    "value"
                            );

                    if (currentValue == null) {
                        currentValue = "";
                    }

                    return value.equals(
                            currentValue
                    );
                }
        );
    }

    private boolean isVisible(
            By locator
    ) {

        List<WebElement> elements =
                driver.findElements(
                        locator
                );

        for (WebElement element : elements) {

            try {

                if (element.isDisplayed()) {
                    return true;
                }

            } catch (Exception ignored) {
            }
        }

        return false;
    }

    private String xpathLiteral(
            String value
    ) {

        if (!value.contains("'")) {

            return "'"
                    + value
                    + "'";
        }

        if (!value.contains("\"")) {

            return "\""
                    + value
                    + "\"";
        }

        String[] parts =
                value.split(
                        "'",
                        -1
                );

        StringBuilder result =
                new StringBuilder(
                        "concat("
                );

        for (int i = 0;
             i < parts.length;
             i++) {

            if (i > 0) {

                result.append(
                        ", \"'\", "
                );
            }

            result.append(
                    "'"
            );

            result.append(
                    parts[i]
            );

            result.append(
                    "'"
            );
        }

        result.append(
                ")"
        );

        return result.toString();
    }

    private static class RecipientCandidate {

        private final String name;
        private final int occurrence;

        private RecipientCandidate(
                String name,
                int occurrence
        ) {

            this.name =
                    name;

            this.occurrence =
                    occurrence;
        }
    }
}