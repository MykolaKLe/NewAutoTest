package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.core.TestData;
import ringotel.model.Contact;
import ringotel.model.User;

public class ContactTests extends TestBase {

    private User user;

    @BeforeMethod
    public void loginPrecondition() {

        user =
                TestData.getUser1();

        logger.info(
                "Logging in {}",
                user.getUsername()
        );

        app.getUser()
                .login(
                        user
                );

        logger.info(
                "{} logged in successfully",
                user.getUsername()
        );
    }

    @Test
    public void createContactPositiveTest() {

        String timestamp =
                String.valueOf(
                        System.currentTimeMillis()
                );

        String uniqueName =
                "AutoTest "
                        + timestamp;

        String lastDigits =
                timestamp.substring(
                        timestamp.length() - 7
                );

        String uniquePhone =
                "+1202"
                        + lastDigits;

        String uniqueEmail =
                "autotest"
                        + timestamp
                        + "@example.com";

        Contact contact =
                new Contact()
                        .setFullName(
                                uniqueName
                        )
                        .setJobTitle(
                                "QA Automation"
                        )
                        .setCompany(
                                "Test Company"
                        )
                        .setPhone(
                                uniquePhone
                        )
                        .setEmail(
                                uniqueEmail
                        )
                        .setNotes(
                                "Created by Selenium test"
                        );

        logger.info(
                "Creating contact: {}",
                contact.getFullName()
        );

        logger.info(
                "Contact phone: {}",
                contact.getPhone()
        );

        logger.info(
                "Contact email: {}",
                contact.getEmail()
        );

        app.getContact()
                .createContact(
                        contact
                );

        logger.info(
                "Create Contact button clicked"
        );

        logger.info(
                "Contact created: {}",
                contact.getFullName()
        );

        String createdScreenshot =
                app.getContact()
                        .takeScreenShot();

        logger.info(
                "Contact creation evidence -> {}",
                createdScreenshot
        );

        logger.info(
                "Searching for created contact: {}",
                contact.getFullName()
        );

        boolean contactPresent =
                app.getContact()
                        .isContactPresent(
                                contact.getFullName()
                        );

        Assert.assertTrue(
                contactPresent,
                "Created contact was not found: "
                        + contact.getFullName()
        );

        logger.info(
                "Contact found in search: {}",
                contact.getFullName()
        );

        String foundScreenshot =
                app.getContact()
                        .takeScreenShot();

        logger.info(
                "Contact search evidence -> {}",
                foundScreenshot
        );

        logger.info(
                "Clicking found contact: {}",
                contact.getFullName()
        );

        app.getContact()
                .openContact(
                        contact.getFullName()
                );

        logger.info(
                "Contact opened: {}",
                contact.getFullName()
        );

        String openedScreenshot =
                app.getContact()
                        .takeScreenShot();

        logger.info(
                "Opened contact evidence -> {}",
                openedScreenshot
        );

        logger.info(
                "Opening actions menu for contact: {}",
                contact.getFullName()
        );

        app.getContact()
                .openContactActions();

        logger.info(
                "Contact actions menu opened"
        );

        logger.info(
                "Clicking Delete Contact"
        );

        app.getContact()
                .openDeleteContactDialog();

        Assert.assertTrue(
                app.getContact()
                        .isDeleteConfirmationVisible(),
                "Delete confirmation dialog was not opened for "
                        + contact.getFullName()
        );

        logger.info(
                "Delete Contact button clicked"
        );

        logger.info(
                "Delete confirmation dialog is visible"
        );

        String deleteDialogScreenshot =
                app.getContact()
                        .takeScreenShot();

        logger.info(
                "Delete confirmation evidence -> {}",
                deleteDialogScreenshot
        );

        logger.info(
                "Confirming deletion of contact: {}",
                contact.getFullName()
        );

        app.getContact()
                .confirmDeleteContact();

        logger.info(
                "Delete confirmation button clicked"
        );

        logger.info(
                "Contact deletion submitted: {}",
                contact.getFullName()
        );

        logger.info(
                "Searching for deleted contact: {}",
                contact.getFullName()
        );

        boolean contactAbsent =
                app.getContact()
                        .isContactAbsent(
                                contact.getFullName()
                        );

        Assert.assertTrue(
                contactAbsent,
                "Contact is still present after deletion: "
                        + contact.getFullName()
        );

        logger.info(
                "Deleted contact was not found in search: {}",
                contact.getFullName()
        );

        String deletedScreenshot =
                app.getContact()
                        .takeScreenShot();

        logger.info(
                "Contact deletion evidence -> {}",
                deletedScreenshot
        );

        logger.info(
                "Contact create, search and delete scenario completed successfully for {}",
                contact.getFullName()
        );
    }
}