package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.model.User;

public class PresenceTests extends TestBase {

    private final User user4321 =
            new User()
                    .setDomain("testwebsoftphone")
                    .setUsername("4321")
                    .setPassword("obFxbmYKYy9pwjAD");

    @Test
    public void changePresenceStatuses() {

        app.getUser()
                .login(user4321);

        app.getPresence()
                .setBusy();

        Assert.assertTrue(
                app.getPresence().isBusy()
        );

        app.getPresence().pause(1500);

        app.getPresence()
                .setAtDesk();

        Assert.assertTrue(
                app.getPresence().isAtDesk()
        );

        app.getPresence().pause(1500);

        app.getPresence()
                .setOnline();

        Assert.assertTrue(
                app.getPresence().isOnline()
        );

        app.getPresence().pause(1500);
    }
}