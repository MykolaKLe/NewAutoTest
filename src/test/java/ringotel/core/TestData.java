package ringotel.core;

import ringotel.model.User;

import java.util.ArrayList;
import java.util.List;

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
                        getValue(
                                "ringotel.domain",
                                "RINGOTEL_DOMAIN",
                                DEFAULT_DOMAIN
                        )
                )
                .setUsername(
                        getValue(
                                "ringotel.user1",
                                "RINGOTEL_USER_1",
                                DEFAULT_USER_1
                        )
                )
                .setPassword(
                        getValue(
                                "ringotel.pass1",
                                "RINGOTEL_PASS_1",
                                DEFAULT_PASSWORD_1
                        )
                );
    }

    public static User getUser2() {

        return new User()
                .setDomain(
                        getValue(
                                "ringotel.domain",
                                "RINGOTEL_DOMAIN",
                                DEFAULT_DOMAIN
                        )
                )
                .setUsername(
                        getValue(
                                "ringotel.user2",
                                "RINGOTEL_USER_2",
                                DEFAULT_USER_2
                        )
                )
                .setPassword(
                        getValue(
                                "ringotel.pass2",
                                "RINGOTEL_PASS_2",
                                DEFAULT_PASSWORD_2
                        )
                );
    }

    public static List<User> getLoginUsers() {

        List<User> users =
                new ArrayList<>();

        users.add(
                getUser1()
        );

        if (shouldUseSecondUser()) {

            users.add(
                    getUser2()
            );
        }

        return users;
    }

    public static boolean shouldUseSecondUser() {

        String mode =
                getOptionalValue(
                        "ringotel.mode",
                        "RINGOTEL_TEST_MODE"
                );

        if (mode != null) {

            if (mode.equalsIgnoreCase("one")) {
                return false;
            }

            if (mode.equalsIgnoreCase("two")) {
                return true;
            }
        }

        boolean user1Provided =
                hasValue(
                        "ringotel.user1",
                        "RINGOTEL_USER_1"
                );

        boolean user2Provided =
                hasValue(
                        "ringotel.user2",
                        "RINGOTEL_USER_2"
                );

        if (user1Provided) {
            return user2Provided;
        }

        return true;
    }

    private static String getValue(
            String systemProperty,
            String environmentVariable,
            String defaultValue
    ) {

        String systemValue =
                System.getProperty(
                        systemProperty
                );

        if (systemValue != null
                && !systemValue.isBlank()) {

            return systemValue.trim();
        }

        String environmentValue =
                System.getenv(
                        environmentVariable
                );

        if (environmentValue != null
                && !environmentValue.isBlank()) {

            return environmentValue.trim();
        }

        return defaultValue;
    }

    private static String getOptionalValue(
            String systemProperty,
            String environmentVariable
    ) {

        String systemValue =
                System.getProperty(
                        systemProperty
                );

        if (systemValue != null
                && !systemValue.isBlank()) {

            return systemValue.trim();
        }

        String environmentValue =
                System.getenv(
                        environmentVariable
                );

        if (environmentValue != null
                && !environmentValue.isBlank()) {

            return environmentValue.trim();
        }

        return null;
    }

    private static boolean hasValue(
            String systemProperty,
            String environmentVariable
    ) {

        return getOptionalValue(
                systemProperty,
                environmentVariable
        ) != null;
    }
}