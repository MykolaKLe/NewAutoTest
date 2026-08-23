package ringotel.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ringotel.core.BaseHelper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ChatHelper extends BaseHelper {

    private final By contactsButton =
            By.cssSelector(
                    "button[aria-label='Contacts']"
            );

    private final By contactsTab =
            By.xpath(
                    "//button[.//span[normalize-space()='Contacts' or normalize-space()='Контакти']]"
            );

    private final By contactsSearch =
            By.cssSelector(
                    "input[aria-label='Search'], input[aria-label='Пошук']"
            );

    private final By openCreateMenuButton =
            By.cssSelector(
                    "button[aria-label='Open create menu'], " +
                            "button[aria-label='Відкрити меню створення']"
            );

    private final By newMessageButton =
            By.cssSelector(
                    "button[data-item-id='message']"
            );

    private final By newMessageSearch =
            By.cssSelector(
                    "input[aria-label='Пошук контактів'], " +
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

        click(contactsButton);

        return this;
    }

    public ChatHelper openContactsTab() {

        click(contactsTab);

        return this;
    }

    public ChatHelper searchContact(String name) {

        type(
                contactsSearch,
                name
        );

        return this;
    }

    public ChatHelper openContact(String name) {

        By contact =
                By.xpath(
                        "//*[normalize-space()='"
                                + name
                                + "']"
                );

        click(contact);

        waitForElement(messageInput);

        return this;
    }

    public ChatHelper openContactFromContacts(
            String name
    ) {

        openContacts();

        openContactsTab();

        searchContact(name);

        openContact(name);

        return this;
    }

    public ChatHelper openNewMessage() {

        click(openCreateMenuButton);

        click(newMessageButton);

        waitForElement(newMessageSearch);

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
        }

        return this;
    }

    public ChatHelper searchNewMessageContact(
            String name
    ) {

        type(
                newMessageSearch,
                name
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

        String expectedName =
                normalizeContactName(
                        name
                );

        WebElement contact =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(10)
                ).until(d -> {

                    List<WebElement> rows =
                            d.findElements(
                                    newMessageContactRows
                            );

                    int currentOccurrence = 0;

                    for (WebElement row : rows) {

                        if (!row.isDisplayed()) {
                            continue;
                        }

                        String rowName =
                                extractContactName(
                                        row
                                );

                        if (rowName.equals(
                                expectedName
                        )) {

                            currentOccurrence++;

                            if (currentOccurrence
                                    == occurrence) {

                                return row;
                            }
                        }
                    }

                    return null;
                });

        contact.click();

        pause(700);

        return this;
    }

    public ChatHelper openContactFromNewMessage(
            String name
    ) {

        openNewMessage();

        searchNewMessageContact(
                name
        );

        selectNewMessageContact(
                name
        );

        return this;
    }

    public boolean isMessageInputAvailable(
            int seconds
    ) {

        try {

            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(seconds)
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
                        "//main//*[normalize-space()='"
                                + message
                                + "']"
                );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        messageLocator
                )
        );

        return this;
    }

    public List<String> getNewMessageContactNames() {

        waitForElement(
                newMessageSearch
        );

        wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        newMessageContactRows
                )
        );

        List<WebElement> rows =
                driver.findElements(
                        newMessageContactRows
                );

        List<String> contacts =
                new ArrayList<>();

        for (WebElement row : rows) {

            if (!row.isDisplayed()) {
                continue;
            }

            String name =
                    extractContactName(
                            row
                    );

            if (!name.isEmpty()) {
                contacts.add(name);
            }
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
                wait.until(d -> {

                    List<WebElement> chats =
                            d.findElements(
                                    chatRows
                            );

                    for (WebElement element : chats) {

                        if (element.isDisplayed()) {
                            return element;
                        }
                    }

                    return null;
                });

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
                                "and ./div[@data-id='"
                                + chatId
                                + "']]"
                );

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        selectedChat
                )
        );

        pause(300);

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

            if (text.equalsIgnoreCase(
                    "test"
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

        if (isVisible(deleteChatButton)) {
            return this;
        }

        List<WebElement> buttons =
                driver.findElements(
                        possibleChatMenuButtons
                );

        for (WebElement button : buttons) {

            if (!button.isDisplayed()) {
                continue;
            }

            try {

                button.click();

                pause(250);

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

                    pause(150);
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
                    d -> !isChatPresent(
                            chatId
                    )
            );

        } catch (TimeoutException e) {

            throw new RuntimeException(
                    "Chat was not deleted. Chat id: "
                            + chatId
            );
        }

        pause(400);

        return this;
    }

    private boolean isChatPresent(
            String chatId
    ) {

        By chat =
                By.xpath(
                        "//button[@aria-pressed " +
                                "and ./div[@data-id='"
                                + chatId
                                + "']]"
                );

        return !driver.findElements(
                chat
        ).isEmpty();
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
            String name
    ) {

        if (name == null) {
            return "";
        }

        return name
                .replace(
                        "\uFFFC",
                        ""
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
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
}