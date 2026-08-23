package ringotel.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ringotel.core.TestBase;
import ringotel.model.Contact;
import ringotel.model.User;

public class ContactTests extends TestBase {

    @BeforeMethod
    public void loginPrecondition() {

        User user =
                new User()
                        .setDomain("testwebsoftphone")
                        .setUsername("1234")
                        .setPassword("R0eE6jAMUmyck7uD");

        app.getUser().login(user);
    }


    @Test
    public void createContactPositiveTest() {

        String timestamp =
                String.valueOf(
                        System.currentTimeMillis()
                );

        String uniqueName =
                "AutoTest " + timestamp;

        String lastDigits =
                timestamp.substring(
                        timestamp.length() - 7
                );

        String uniquePhone =
                "+1202" + lastDigits;

        String uniqueEmail =
                "autotest"
                        + timestamp
                        + "@example.com";


        Contact contact =
                new Contact()
                        .setFullName(
                                uniqueName
                        )
                        .setJobTitle(
                                "QA Automation"
                        )
                        .setCompany(
                                "Test Company"
                        )
                        .setPhone(
                                uniquePhone
                        )
                        .setEmail(
                                uniqueEmail
                        )
                        .setNotes(
                                "Created by Selenium test"
                        );


        System.out.println(
                "================================"
        );

        System.out.println(
                "Creating contact: "
                        + uniqueName
        );

        System.out.println(
                "Phone: "
                        + uniquePhone
        );

        System.out.println(
                "Email: "
                        + uniqueEmail
        );

        System.out.println(
                "================================"
        );


        app.getContact()
                .createContact(contact);


        boolean contactPresent =
                app.getContact()
                        .isContactPresent(
                                contact.getFullName()
                        );


        Assert.assertTrue(
                contactPresent,
                "Created contact was not found: "
                        + contact.getFullName()
        );
    }
}