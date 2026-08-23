package ringotel.utils;

import org.testng.annotations.DataProvider;
import ringotel.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyDataProviders {

    @DataProvider
    public Iterator<Object[]> ringotelUsers() {

        List<Object[]> users = new ArrayList<>();

        users.add(
                new Object[]{
                        new User()
                                .setDomain("testwebsoftphone")
                                .setUsername("4321")
                                .setPassword("obFxbmYKYy9pwjAD")
                }
        );

        users.add(
                new Object[]{
                        new User()
                                .setDomain("testwebsoftphone")
                                .setUsername("1234")
                                .setPassword("R0eE6jAMUmyck7uD")
                }
        );

        return users.iterator();
    }
}