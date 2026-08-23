package ringotel.tests;

import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.model.User;

public class ChatTests extends TestBase {

    private final User user4321 =
            new User()
                    .setDomain("testwebsoftphone")
                    .setUsername("4321")
                    .setPassword("obFxbmYKYy9pwjAD");

    @Test
    public void sendMessageToTzipyBohemFromContacts() {

        app.getUser()
                .login(user4321);

        String message =
                "AutoTest "
                        + System.currentTimeMillis();

        app.getChat()
                .openContactFromContacts(
                        "Tzipy Bohem"
                )
                .sendMessage(
                        message
                )
                .verifyMessage(
                        message
                );
    }

    @Test
    public void checkMessagingForImportedContact() {

        app.getUser()
                .login(user4321);

        String contact =
                "test1import1";

        app.getChat()
                .openContactFromNewMessage(
                        contact
                );

        if (!app.getChat()
                .isMessageInputAvailable(2)) {

            String screenshot =
                    app.getChat()
                            .takeScreenShot();

            logger.warn(
                    "Messaging is not available for contact '{}'. Screenshot -> {}",
                    contact,
                    screenshot
            );

            return;
        }

        String message =
                "AutoTest "
                        + System.currentTimeMillis();

        app.getChat()
                .sendMessage(
                        message
                )
                .verifyMessage(
                        message
                );

        logger.info(
                "Message successfully sent to contact '{}'",
                contact
        );
    }
}