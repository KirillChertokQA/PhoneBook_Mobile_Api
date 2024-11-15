package mobile_tests;

import config.AppiumConfig;
import data_provider.ContactDP;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.UserDtoLombok;
import helper.HelperApiMobile;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.*;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.*;

public class AddNewContactTests extends AppiumConfig {

    UserDtoLombok user = UserDtoLombok.builder()
            .username(getProperty("data.properties", "email"))
            .password(getProperty("data.properties", "password"))
            .build();

    AddNewContactsScreen addNewContactsScreen;

    @BeforeMethod
    public void loginAndGoToAddNewContactScreen() {
        new SplashScreen(driver).goToAuthScreen(5);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        new ContactsScreen(driver).clickBtnAddNewContact();
        addNewContactsScreen = new AddNewContactsScreen(driver);
    }

    @Test
    public void addNewContactPositiveTest() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone(generatePhone(12))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        Assert.assertTrue(new ContactsScreen(driver).validatePopUpMessage());

    }

    @Test
    public void addNewContactPositiveTestValidateContactApi() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone(generatePhone(12))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        //Assert.assertTrue(new ContactsScreen(driver).validatePopUpMessage());
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGet.as(ContactsDto.class);

        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
                break;
            }
        }
        System.out.println("-->" + flag);
        Assert.assertTrue(flag);

    }

    @Test
    public void addNewContactNegativeTest_wrongEmail() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .email(generateString(10))
                .phone(generatePhone(12))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        Assert.assertTrue(new ErrorScreen(driver).validateErrorMessage("email=must be a well-formed email address", 5));

    }

    @Test
    public void addNewContactNegativeTest_nameIsEmpty() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("")
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone(generatePhone(12))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        Assert.assertTrue(new ErrorScreen(driver).validateErrorMessage("name=must not be blank", 5));

    }

    @Test
    public void addNewContactNegativeTest_fieldPhoneIsNotValid() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone("@@@@123dfgtrq!")
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        Assert.assertTrue(new ErrorScreen(driver)
                .validateErrorMessage("phone=Phone number must contain only digits! And length min 10, max 15!", 5));

    }

    @Test(dataProvider = "addNewContactDPFile", dataProviderClass = ContactDP.class)
    public void addNewContactNegativeTest_emptyField(ContactDtoLombok contact) {
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        Assert.assertTrue(new ErrorScreen(driver).validateErrorMessage("must not be blank", 5)
                || new ErrorScreen(driver).validateErrorMessage("well-formed email address", 5)
                || new ErrorScreen(driver).validateErrorMessage("phone number must contain", 5));
    }

    @Test
    public void addNewContactNegativeTest_duplicateContact() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone(generatePhone(12))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        //Assert.assertTrue(new ContactsScreen(driver).validatePopUpMessage());
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGet.as(ContactsDto.class);
    }
}