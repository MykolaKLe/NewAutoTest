package ringotel.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ringotel.core.BaseHelper;
import ringotel.model.Contact;

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
                    "//button[.//span[normalize-space()='Contacts']]"
            );


    private final By searchInput =
            By.cssSelector(
                    "input[aria-label='Search']"
            );


    private final By accountButton =
            By.cssSelector(
                    "button[aria-label='Account']"
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
                        ExpectedConditions.elementToBeClickable(
                                contactsTabButton
                        )
                );



        contactsTab.click();



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

        System.out.println(
                "CONTACTS TAB OPENED"
        );
    }


    public void searchContact(String name) {

        WebElement search =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                searchInput
                        )
                );

        search.click();
        search.clear();
        search.sendKeys(name);


        wait.until(
                ExpectedConditions.attributeToBe(
                        searchInput,
                        "value",
                        name
                )
        );

        System.out.println(
                "SEARCH CONTACT: " + name
        );
    }


    public boolean isContactPresent(String name) {


        refreshApplication();



        openContacts();


        searchContact(name);


        By contactName =
                By.xpath(
                        "//*[normalize-space()=\"" +
                                name +
                                "\"]"
                );


        try {

            WebElement contact =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    contactName
                            )
                    );


            System.out.println(
                    "CONTACT FOUND: "
                            + contact.getText()
            );

            return true;

        } catch (TimeoutException e) {

            System.out.println(
                    "CONTACT NOT FOUND: "
                            + name
            );

            System.out.println(
                    "SEARCH VALUE: "
                            + driver
                            .findElement(searchInput)
                            .getAttribute("value")
            );

            System.out.println(
                    "CURRENT URL: "
                            + driver.getCurrentUrl()
            );

            return false;
        }
    }
}