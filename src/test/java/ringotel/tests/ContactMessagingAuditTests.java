package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.core.TestData;
import ringotel.model.Contact;
import ringotel.model.User;

import java.util.ArrayList;
import java.util.List;

public class ContactMessagingAuditTests extends TestBase {

    @Test
    public void checkMessagingAvailabilityForAllContacts() {

        User user =
                TestData.getUser1();

        String extension =
                user.getUsername();

        logger.info(
                "CONTACTS ONLY messaging audit started"
        );

        logger.info(
                "Logging in {}",
                extension
        );

        app.getUser()
                .login(user);

        logger.info(
                "{} logged in successfully",
                extension
        );

        logger.info(
                "Opening Contacts"
        );

        List<Contact> contacts =
                app.getContact()
                        .getContactsForMessagingAudit();

        Assert.assertFalse(
                contacts.isEmpty(),
                "No contacts found"
        );

        logger.info(
                "Contacts opened and expanded"
        );

        logger.info(
                "Contacts found for messaging audit: {}",
                contacts.size()
        );

        int withPhoneNumber = 0;
        int withoutPhoneNumber = 0;
        int messagingAvailable = 0;
        int messagingUnavailable = 0;
        int failedToOpen = 0;

        List<String> noPhoneContacts =
                new ArrayList<>();

        List<String> unavailableContacts =
                new ArrayList<>();

        List<String> failedContacts =
                new ArrayList<>();

        for (Contact contact : contacts) {

            String name =
                    contact.getFullName();

            int occurrence =
                    contact.getOccurrence();

            if (contact.hasPhoneNumber()) {

                withPhoneNumber++;

                logger.info(
                        "Contact '{}' occurrence {} - PHONE NUMBER PRESENT",
                        name,
                        occurrence
                );

            } else {

                withoutPhoneNumber++;

                noPhoneContacts.add(
                        name
                                + " #"
                                + occurrence
                );

                logger.info(
                        "Contact '{}' occurrence {} - NO PHONE NUMBER",
                        name,
                        occurrence
                );
            }

            try {

                logger.info(
                        "Opening contact '{}' occurrence {}",
                        name,
                        occurrence
                );

                app.getContact()
                        .openContactForAudit(
                                name,
                                occurrence
                        );

                logger.info(
                        "Contact '{}' opened",
                        name
                );

                boolean messageInputAvailable =
                        app.getChat()
                                .isMessageInputAvailable(
                                        2
                                );

                if (messageInputAvailable) {

                    messagingAvailable++;

                    if (contact.hasPhoneNumber()) {

                        logger.info(
                                "Messaging AVAILABLE for '{}' - PHONE NUMBER PRESENT",
                                name
                        );

                    } else {

                        logger.info(
                                "Messaging AVAILABLE for '{}' - NO PHONE NUMBER",
                                name
                        );
                    }

                } else {

                    messagingUnavailable++;

                    unavailableContacts.add(
                            name
                                    + " #"
                                    + occurrence
                    );

                    if (contact.hasPhoneNumber()) {

                        logger.warn(
                                "Messaging UNAVAILABLE for '{}' - PHONE NUMBER PRESENT",
                                name
                        );

                    } else {

                        logger.info(
                                "Messaging UNAVAILABLE for '{}' - NO PHONE NUMBER",
                                name
                        );
                    }
                }

            } catch (Exception e) {

                failedToOpen++;

                failedContacts.add(
                        name
                                + " #"
                                + occurrence
                );

                logger.warn(
                        "Could not open contact '{}' occurrence {}",
                        name,
                        occurrence
                );

                String screenshot =
                        app.getContact()
                                .takeScreenShot();

                logger.warn(
                        "Contact opening evidence -> {}",
                        screenshot
                );
            }
        }

        logger.info(
                "========================================"
        );

        logger.info(
                "Messaging audit completed for user {}",
                extension
        );

        logger.info(
                "Total contacts: {}",
                contacts.size()
        );

        logger.info(
                "With phone number: {}",
                withPhoneNumber
        );

        logger.info(
                "Without phone number: {}",
                withoutPhoneNumber
        );

        logger.info(
                "Messaging available: {}",
                messagingAvailable
        );

        logger.info(
                "Messaging unavailable: {}",
                messagingUnavailable
        );

        logger.info(
                "Failed to open: {}",
                failedToOpen
        );

        if (!noPhoneContacts.isEmpty()) {

            logger.info(
                    "NO PHONE NUMBER contacts: {}",
                    noPhoneContacts
            );
        }

        if (!unavailableContacts.isEmpty()) {

            logger.warn(
                    "Messaging unavailable contacts: {}",
                    unavailableContacts
            );
        }

        if (!failedContacts.isEmpty()) {

            logger.error(
                    "Contacts that could not be audited: {}",
                    failedContacts
            );
        }

        logger.info(
                "========================================"
        );

        Assert.assertEquals(
                failedToOpen,
                0,
                "Some contacts could not be opened: "
                        + failedContacts
        );

        Assert.assertEquals(
                messagingAvailable
                        + messagingUnavailable,
                contacts.size(),
                "Not all contacts were audited"
        );
    }
}