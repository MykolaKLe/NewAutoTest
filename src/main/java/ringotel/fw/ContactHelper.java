package ringotel.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ringotel.core.BaseHelper;
import ringotel.model.Contact;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactHelper extends BaseHelper {

    private final By openCreateMenuButton =
            By.cssSelector(
                    "button[aria-label='Open create menu']"
            );

    private final By createContactMenuItem =
            By.xpath(
                    "//*[normalize-space()='Create Contact']"
            );

    private final By newContactTitle =
            By.xpath(
                    "//*[normalize-space()='New Contact']"
            );

    private final By fullNameInput =
            By.xpath(
                    "(//*[normalize-space()='Full name']" +
                            "/following::input[not(@type='file')][1])[1]"
            );

    private final By jobTitleInput =
            By.cssSelector(
                    "input[placeholder='Add job title']"
            );

    private final By companyInput =
            By.cssSelector(
                    "input[placeholder='Add company']"
            );

    private final By phoneInput =
            By.xpath(
                    "(//*[normalize-space()='Phone']" +
                            "/following::input[1])[1]"
            );

    private final By emailInput =
            By.xpath(
                    "(//*[normalize-space()='Email']" +
                            "/following::input[1])[1]"
            );

    private final By notesInput =
            By.cssSelector(
                    "textarea[placeholder='Add note']"
            );

    private final By createContactButton =
            By.xpath(
                    "//button[normalize-space()='Create Contact']"
            );

    private final By contactsNavButton =
            By.cssSelector(
                    "button[aria-label='Contacts']"
            );

    private final By contactsTabButton =
            By.xpath(
                    "//button[" +
                            "@aria-pressed" +
                            " and .//span[normalize-space()='Contacts']" +
                            "]"
            );

    private final By contactsSectionToggle =
            By.xpath(
                    "//button[" +
                            "@aria-expanded" +
                            " and .//span[normalize-space()='Contacts']" +
                            "]"
            );

    private final By searchInput =
            By.cssSelector(
                    "input[aria-label='Search']"
            );

    private final By accountButton =
            By.cssSelector(
                    "button[aria-label='Account']"
            );

    private final By contactRows =
            By.cssSelector(
                    "div[data-row-shell='true'][data-interactive='true']"
            );

    private final By contactNameInRow =
            By.xpath(
                    "./button[1]/span[2]/span[1]"
            );

    private final By contactBodyButton =
            By.xpath(
                    "./button[1]"
            );

    private final By phoneActionButton =
            By.xpath(
                    "./span[@data-contact-list-item-actions='true']/button"
            );

    private final By selectedContactActionsButton =
            By.xpath(
                    "//div[@data-row-shell='true']" +
                            "[.//button[@aria-current='true']]" +
                            "//span[@data-contact-list-item-actions='true']" +
                            "//button[@aria-haspopup='menu']"
            );

    private final By deleteContactMenuItem =
            By.cssSelector(
                    "button[data-item-id='delete-contact']"
            );

    private final By deleteConfirmationDialog =
            By.cssSelector(
                    "div[role='dialog'][aria-modal='true']"
            );

    private final By confirmDeleteButton =
            By.xpath(
                    "//div[@role='dialog' and @aria-modal='true']" +
                            "//footer/button[last()]"
            );

    public ContactHelper(WebDriver driver) {
        super(driver);
    }

    public void openCreateContactForm() {

        click(openCreateMenuButton);

        click(createContactMenuItem);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        newContactTitle
                )
        );
    }

    public void fillContactForm(Contact contact) {

        type(
                fullNameInput,
                contact.getFullName()
        );

        if (contact.getJobTitle() != null) {

            type(
                    jobTitleInput,
                    contact.getJobTitle()
            );
        }

        if (contact.getCompany() != null) {

            type(
                    companyInput,
                    contact.getCompany()
            );
        }

        if (contact.getPhone() != null) {

            type(
                    phoneInput,
                    contact.getPhone()
            );
        }

        if (contact.getEmail() != null) {

            type(
                    emailInput,
                    contact.getEmail()
            );
        }

        if (contact.getNotes() != null) {

            type(
                    notesInput,
                    contact.getNotes()
            );
        }
    }

    public void clickOnCreateContactButton() {

        click(createContactButton);

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        newContactTitle
                )
        );
    }

    public void createContact(Contact contact) {

        openCreateContactForm();

        fillContactForm(contact);

        clickOnCreateContactButton();
    }

    public void refreshApplication() {

        driver.navigate().refresh();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        accountButton
                )
        );
    }

    public void openContacts() {

        WebElement contactsNavigation =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                contactsNavButton
                        )
                );

        contactsNavigation.click();

        WebElement contactsTab =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                contactsTabButton
                        )
                );

        if (!"true".equals(
                contactsTab.getAttribute(
                        "aria-pressed"
                )
        )) {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            contactsTabButton
                    )
            ).click();
        }

        wait.until(
                ExpectedConditions.attributeToBe(
                        contactsTabButton,
                        "aria-pressed",
                        "true"
                )
        );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        searchInput
                )
        );
    }

    public void openContactsSection() {

        WebElement contactsSection =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                contactsSectionToggle
                        )
                );

        if (!"true".equals(
                contactsSection.getAttribute(
                        "aria-expanded"
                )
        )) {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            contactsSectionToggle
                    )
            ).click();
        }

        wait.until(
                ExpectedConditions.attributeToBe(
                        contactsSectionToggle,
                        "aria-expanded",
                        "true"
                )
        );

        wait.until(
                driver -> !driver.findElements(
                        contactRows
                ).isEmpty()
        );
    }

    public void searchContact(String name) {

        WebElement search =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                searchInput
                        )
                );

        clearInput(search);

        search.sendKeys(
                name
        );

        wait.until(
                driver -> {

                    WebElement input =
                            driver.findElement(
                                    searchInput
                            );

                    String value =
                            input.getDomProperty(
                                    "value"
                            );

                    return name.equals(
                            value
                    );
                }
        );
    }

    public void clearSearch() {

        WebElement search =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                searchInput
                        )
                );

        String currentValue =
                search.getDomProperty(
                        "value"
                );

        if (currentValue != null
                && !currentValue.isEmpty()) {

            clearInput(
                    search
            );
        }

        wait.until(
                driver -> {

                    WebElement input =
                            driver.findElement(
                                    searchInput
                            );

                    String value =
                            input.getDomProperty(
                                    "value"
                            );

                    return value == null
                            || value.isEmpty();
                }
        );
    }

    public boolean isContactPresent(String name) {

        refreshApplication();

        openContacts();

        searchContact(name);

        return waitForContactVisible(
                name
        );
    }

    public boolean waitForContactVisible(String name) {

        try {

            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(10)
            ).until(
                    driver -> !findContactRows(
                            name
                    ).isEmpty()
            );

            return true;

        } catch (TimeoutException e) {

            return false;
        }
    }

    public void openContact(String name) {

        List<WebElement> rows =
                wait.until(
                        driver -> {

                            List<WebElement> matches =
                                    findContactRows(
                                            name
                                    );

                            if (matches.isEmpty()) {
                                return null;
                            }

                            return matches;
                        }
                );

        WebElement row =
                rows.get(0);

        WebElement button =
                row.findElement(
                        contactBodyButton
                );

        wait.until(
                driver -> button.isDisplayed()
                        && button.isEnabled()
        );

        button.click();

        waitForContactMainView(
                name
        );
    }

    public void openContactForAudit(
            String name,
            int occurrence
    ) {

        clearSearch();

        WebElement contactButton =
                wait.until(
                        driver -> {

                            List<WebElement> rows =
                                    findContactRows(
                                            name
                                    );

                            if (rows.size()
                                    < occurrence) {

                                return null;
                            }

                            WebElement row =
                                    rows.get(
                                            occurrence - 1
                                    );

                            WebElement button =
                                    row.findElement(
                                            contactBodyButton
                                    );

                            if (!button.isDisplayed()
                                    || !button.isEnabled()) {

                                return null;
                            }

                            return button;
                        }
                );

        contactButton.click();

        waitForContactMainView(
                name
        );
    }

    public void openContactActions() {

        WebElement actionsButton =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                selectedContactActionsButton
                        )
                );

        actionsButton.click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        deleteContactMenuItem
                )
        );
    }

    public void openDeleteContactDialog() {

        click(
                deleteContactMenuItem
        );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        deleteConfirmationDialog
                )
        );
    }

    public boolean isDeleteConfirmationVisible() {

        List<WebElement> dialogs =
                driver.findElements(
                        deleteConfirmationDialog
                );

        for (WebElement dialog : dialogs) {

            try {

                if (dialog.isDisplayed()) {
                    return true;
                }

            } catch (Exception ignored) {
            }
        }

        return false;
    }

    public void confirmDeleteContact() {

        click(
                confirmDeleteButton
        );

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        deleteConfirmationDialog
                )
        );
    }

    public boolean isContactAbsent(String name) {

        refreshApplication();

        openContacts();

        searchContact(
                name
        );

        try {

            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(5)
            ).until(
                    driver -> !findContactRows(
                            name
                    ).isEmpty()
            );

            return false;

        } catch (TimeoutException e) {

            return true;
        }
    }

    public List<Contact> getContactsForMessagingAudit() {

        openContacts();

        openContactsSection();

        clearSearch();

        List<WebElement> rows =
                wait.until(
                        driver -> {

                            List<WebElement> elements =
                                    driver.findElements(
                                            contactRows
                                    );

                            if (elements.isEmpty()) {
                                return null;
                            }

                            return elements;
                        }
                );

        List<Contact> contacts =
                new ArrayList<>();

        Map<String, Integer> occurrences =
                new HashMap<>();

        for (WebElement row : rows) {

            try {

                if (!row.isDisplayed()) {
                    continue;
                }

                String name =
                        getContactNameFromRow(
                                row
                        );

                if (name.isBlank()) {
                    continue;
                }

                int occurrence =
                        occurrences.merge(
                                name,
                                1,
                                Integer::sum
                        );

                boolean hasPhoneNumber =
                        !row.findElements(
                                phoneActionButton
                        ).isEmpty();

                contacts.add(
                        new Contact()
                                .setFullName(
                                        name
                                )
                                .setOccurrence(
                                        occurrence
                                )
                                .setHasPhoneNumber(
                                        hasPhoneNumber
                                )
                );

            } catch (Exception ignored) {
            }
        }

        return contacts;
    }

    private List<WebElement> findContactRows(
            String name
    ) {

        List<WebElement> result =
                new ArrayList<>();

        List<WebElement> rows =
                driver.findElements(
                        contactRows
                );

        String normalizedExpectedName =
                normalizeText(
                        name
                );

        for (WebElement row : rows) {

            try {

                if (!row.isDisplayed()) {
                    continue;
                }

                String rowName =
                        getContactNameFromRow(
                                row
                        );

                if (normalizedExpectedName.equals(
                        normalizeText(
                                rowName
                        )
                )) {

                    result.add(
                            row
                    );
                }

            } catch (Exception ignored) {
            }
        }

        return result;
    }

    private String getContactNameFromRow(
            WebElement row
    ) {

        List<WebElement> names =
                row.findElements(
                        contactNameInRow
                );

        if (names.isEmpty()) {
            return "";
        }

        return normalizeText(
                names.get(0)
                        .getText()
        );
    }

    private void waitForContactMainView(
            String name
    ) {

        String normalizedName =
                normalizeText(
                        name
                );

        wait.until(
                driver -> {

                    List<WebElement> headers =
                            driver.findElements(
                                    By.cssSelector(
                                            "main header[aria-label]"
                                    )
                            );

                    for (WebElement header : headers) {

                        try {

                            if (!header.isDisplayed()) {
                                continue;
                            }

                            String ariaLabel =
                                    normalizeText(
                                            header.getAttribute(
                                                    "aria-label"
                                            )
                                    );

                            if (normalizedName.equals(
                                    ariaLabel
                            )) {

                                return true;
                            }

                        } catch (Exception ignored) {
                        }
                    }

                    return false;
                }
        );
    }

    private String normalizeText(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
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

    private void clearInput(
            WebElement element
    ) {

        element.click();

        Keys modifier =
                System.getProperty(
                                "os.name",
                                ""
                        )
                        .toLowerCase()
                        .contains(
                                "mac"
                        )
                        ? Keys.COMMAND
                        : Keys.CONTROL;

        element.sendKeys(
                Keys.chord(
                        modifier,
                        "a"
                )
        );

        element.sendKeys(
                Keys.BACK_SPACE
        );
    }
}