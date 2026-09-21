package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.core.TestData;
import ringotel.model.User;

public class ChatTests extends TestBase {

    @Test
    public void sendChatMessage() {

        User user =
                TestData.getUser1();

        String recipient =
                getSetting(
                        "ringotel.chat.recipient",
                        "RINGOTEL_CHAT_RECIPIENT",
                        ""
                )
                        .trim();

        int occurrence =
                Integer.parseInt(
                        getSetting(
                                "ringotel.chat.occurrence",
                                "RINGOTEL_CHAT_OCCURRENCE",
                                "1"
                        )
                );

        String message =
                getSetting(
                        "ringotel.chat.message",
                        "RINGOTEL_CHAT_MESSAGE",
                        ""
                );

        boolean deleteAfterSend =
                Boolean.parseBoolean(
                        getSetting(
                                "ringotel.chat.deleteAfterSend",
                                "RINGOTEL_CHAT_DELETE_AFTER_SEND",
                                "true"
                        )
                );

        Assert.assertFalse(
                recipient.isBlank(),
                "Specific chat recipient is required"
        );

        Assert.assertTrue(
                occurrence > 0,
                "Recipient occurrence must be greater than 0"
        );

        if (message.isBlank()) {

            message =
                    "AutoTest "
                            + System.currentTimeMillis();
        }

        logger.info(
                "Specific recipient chat scenario started"
        );

        logger.info(
                "Recipient: {}",
                recipient
        );

        logger.info(
                "Recipient occurrence: {}",
                occurrence
        );

        logger.info(
                "Delete chat after send: {}",
                deleteAfterSend
        );

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

        logger.info(
                "Opening specific recipient '{}' occurrence {}",
                recipient,
                occurrence
        );

        app.getChat()
                .openContactFromNewMessage(
                        recipient,
                        occurrence
                );

        boolean messagingAvailable =
                app.getChat()
                        .isMessageInputAvailable(
                                3
                        );

        if (!messagingAvailable) {

            String screenshot =
                    app.getChat()
                            .takeScreenShot();

            Assert.fail(
                    "Messaging is not available for recipient '"
                            + recipient
                            + "'. Screenshot -> "
                            + screenshot
            );
        }

        logger.info(
                "Messaging is available for '{}'",
                recipient
        );

        logger.info(
                "Sending message to '{}': {}",
                recipient,
                message
        );

        app.getChat()
                .sendMessage(
                        message
                );

        logger.info(
                "Message sent"
        );

        app.getChat()
                .verifyMessage(
                        message
                );

        logger.info(
                "Message verified"
        );

        if (deleteAfterSend) {

            String chatId =
                    app.getChat()
                            .getActiveChatId();

            logger.info(
                    "Active chat id: {}",
                    chatId
            );

            logger.info(
                    "Deleting chat with '{}'",
                    recipient
            );

            app.getChat()
                    .deleteCurrentChat();

            Assert.assertFalse(
                    app.getChat()
                            .isChatPresent(
                                    chatId
                            ),
                    "Deleted chat is still present: "
                            + chatId
            );

            logger.info(
                    "Chat deleted successfully. Chat id: {}",
                    chatId
            );

        } else {

            logger.info(
                    "Chat with '{}' was left in the chat list",
                    recipient
            );
        }

        logger.info(
                "Specific recipient chat scenario completed successfully for '{}'",
                recipient
        );
    }

    private String getSetting(
            String propertyName,
            String environmentName,
            String defaultValue
    ) {

        String propertyValue =
                System.getProperty(
                        propertyName
                );

        if (propertyValue != null
                && !propertyValue.isBlank()) {

            return propertyValue.trim();
        }

        String environmentValue =
                System.getenv(
                        environmentName
                );

        if (environmentValue != null
                && !environmentValue.isBlank()) {

            return environmentValue.trim();
        }

        return defaultValue;
    }
}