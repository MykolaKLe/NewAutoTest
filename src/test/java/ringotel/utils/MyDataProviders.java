package ringotel.utils;

import org.testng.annotations.DataProvider;
import ringotel.core.TestData;
import ringotel.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyDataProviders {

    @DataProvider
    public Iterator<Object[]> ringotelUsers() {

        List<Object[]> testData =
                new ArrayList<>();

        List<User> users =
                TestData.getLoginUsers();

        for (User user : users) {

            testData.add(
                    new Object[]{
                            user
                    }
            );
        }

        return testData.iterator();
    }
}