package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.model.User;

public class ChatCleanupTests extends TestBase {

    private final User user4321 =
            new User()
                    .setDomain("testwebsoftphone")
                    .setUsername("4321")
                    .setPassword("obFxbmYKYy9pwjAD");

    @Test
    public void deleteAllChats() {

        app.getUser()
                .login(user4321);

        int initialCount =
                app.getChat()
                        .getChatCount();

        logger.info(
                "Chats before cleanup: {}",
                initialCount
        );

        int deleted = 0;

        while (app.getChat()
                .hasChats()) {

            int before =
                    app.getChat()
                            .getChatCount();

            app.getChat()
                    .openFirstChat();

            String chatName =
                    app.getChat()
                            .getActiveChatName();

            String chatId =
                    app.getChat()
                            .getActiveChatId();

            logger.info(
                    "Deleting chat '{}' [{}]",
                    chatName,
                    chatId
            );

            app.getChat()
                    .deleteCurrentChat();

            int after =
                    app.getChat()
                            .getChatCount();

            deleted++;

            logger.info(
                    "Deleted chat '{}'. Before: {}, after: {}",
                    chatName,
                    before,
                    after
            );
        }

        Assert.assertEquals(
                app.getChat()
                        .getChatCount(),
                0,
                "Not all chats were deleted"
        );

        logger.info(
                "Chat cleanup completed. Initial: {}, deleted: {}, remaining: {}",
                initialCount,
                deleted,
                app.getChat().getChatCount()
        );
    }
}