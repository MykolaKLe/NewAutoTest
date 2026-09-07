package ringotel.core;

import ringotel.model.User;

public class TestData {

    private static final String DEFAULT_DOMAIN =
            "testwebsoftphone";

    private static final String DEFAULT_USER_1 =
            "4321";

    private static final String DEFAULT_PASSWORD_1 =
            "obFxbmYKYy9pwjAD";

    private static final String DEFAULT_USER_2 =
            "1234";

    private static final String DEFAULT_PASSWORD_2 =
            "R0eE6jAMUmyck7uD";

    public static User getUser1() {

        return new User()
                .setDomain(
                        getProperty(
                                "ringotel.domain",
                                DEFAULT_DOMAIN
                        )
                )
                .setUsername(
                        getProperty(
                                "ringotel.user1",
                                DEFAULT_USER_1
                        )
                )
                .setPassword(
                        getProperty(
                                "ringotel.pass1",
                                DEFAULT_PASSWORD_1
                        )
                );
    }

    public static User getUser2() {

        return new User()
                .setDomain(
                        getProperty(
                                "ringotel.domain",
                                DEFAULT_DOMAIN
                        )
                )
                .setUsername(
                        getProperty(
                                "ringotel.user2",
                                DEFAULT_USER_2
                        )
                )
                .setPassword(
                        getProperty(
                                "ringotel.pass2",
                                DEFAULT_PASSWORD_2
                        )
                );
    }

    private static String getProperty(
            String name,
            String defaultValue
    ) {

        String value =
                System.getProperty(
                        name
                );

        if (value == null
                || value.isBlank()) {

            return defaultValue;
        }

        return value.trim();
    }
}