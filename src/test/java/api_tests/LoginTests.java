package api_tests;

import config.AuthenticationController;
import dto.ErrorMessageDto;
import dto.UserDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.generateEmail;

public class LoginTests extends AuthenticationController {

    SoftAssert softAssert = new SoftAssert();

    @BeforeMethod
    public void registrationPositiveTest() {
        UserDto user = new UserDto(generateEmail(12), "Password123!");
        Assert.assertEquals(requestRegLogin(user, REGISTRATION_PATH).getStatusCode(), 200);
    }

    @Test
    public void loginPositiveTest(){
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                getProperty("data.properties", "password"));
        Assert.assertEquals(requestLogin(user, LOGIN_PATH).getStatusCode(), 200);

    }

    @Test
    public void loginNegativeTest_wrongPassword401(){
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                "wrongpassword");
        Response response = requestLogin(user, LOGIN_PATH);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("Login or Password incorrect"));
        softAssert.assertEquals(response.getStatusCode(), 401);
        softAssert.assertAll();

    }

    @Test
    public void loginNegativeTest_newEmail401(){
        UserDto user = new UserDto(generateEmail(12), "Password123!");
        Response response = requestLogin(user, LOGIN_PATH);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertTrue(errorMessageDto.getError().equals("Unauthorized"));
        softAssert.assertEquals(response.getStatusCode(), 401);
        softAssert.assertAll();

    }

    @Test
    public void loginNegativeTest_WOFillingFields(){
        UserDto user = new UserDto("", "");
        Response response = requestLogin(user, LOGIN_PATH);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("Login or Password incorrect"));
        softAssert.assertEquals(response.getStatusCode(), 401);
        softAssert.assertAll();

    }
}
