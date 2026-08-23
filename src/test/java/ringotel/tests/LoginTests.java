package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ringotel.core.TestBase;
import ringotel.model.User;
import ringotel.utils.MyDataProviders;

public class LoginTests extends TestBase {

    @Test(
            dataProvider = "ringotelUsers",
            dataProviderClass = MyDataProviders.class
    )
    public void loginPositiveTest(User user) {

        app.getUser().login(user);

        Assert.assertTrue(
                app.getUser().isLoggedIn()
        );
    }
}