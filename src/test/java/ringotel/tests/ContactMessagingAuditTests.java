package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactMessagingAuditTests extends TestBase {

    private final User user4321 =
            new User()
                    .setDomain("testwebsoftphone")
                    .setUsername("4321")
                    .setPassword("obFxbmYKYy9pwjAD");

    @Test
    public void checkMessagingAvailabilityForAllContacts() {

        app.getUser()
                .login(user4321);

        app.getChat()
                .openNewMessage();

        List<String> contacts =
                app.getChat()
                        .getNewMessageContactNames();

        Assert.assertFalse(
                contacts.isEmpty(),
                "No contacts found in New Message"
        );

        logger.info(
                "Contacts found for messaging audit: {}",
                contacts.size()
        );

        Map<String, Integer> occurrences =
                new HashMap<>();

        int available = 0;
        int unavailable = 0;
        int sent = 0;
        int deleted = 0;

        for (String contact : contacts) {

            int occurrence =
                    occurrences.merge(
                            contact,
                            1,
                            Integer::sum
                    );

            logger.info(
                    "Checking contact '{}', occurrence {}",
                    contact,
                    occurrence
            );

            app.getChat()
                    .ensureNewMessageWindowOpen()
                    .searchNewMessageContact(
                            contact
                    )
                    .selectNewMessageContact(
                            contact,
                            occurrence
                    );

            if (!app.getChat()
                    .isMessageInputAvailable(2)) {

                unavailable++;

                String screenshot =
                        app.getChat()
                                .takeScreenShot();

                logger.warn(
                        "Messaging is not available for contact '{}'. Screenshot -> {}",
                        contact,
                        screenshot
                );

                continue;
            }

            available++;

            app.getChat()
                    .sendMessage("test")
                    .verifyMessage("test");

            sent++;

            logger.info(
                    "Message successfully sent to contact '{}'",
                    contact
            );

            app.getChat()
                    .pause(500);

            String chatId =
                    app.getChat()
                            .getActiveChatId();

            app.getChat()
                    .deleteCurrentChat();

            deleted++;

            logger.info(
                    "Chat for contact '{}' was deleted. Chat id: {}",
                    contact,
                    chatId
            );
        }

        logger.info(
                "Messaging audit completed. Total: {}, available: {}, unavailable: {}, sent: {}, deleted: {}",
                contacts.size(),
                available,
                unavailable,
                sent,
                deleted
        );

        Assert.assertEquals(
                sent,
                deleted,
                "Some created chats were not deleted"
        );
    }
}