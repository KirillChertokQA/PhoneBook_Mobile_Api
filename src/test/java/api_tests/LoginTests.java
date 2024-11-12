package api_tests;

import config.AuthenticationController;
import dto.ErrorMessageDto;
import dto.TokenDto;
import dto.UserDto;
import dto.UserDtoLombok;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;

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
        UserDtoLombok user = UserDtoLombok.builder()
                .username(getProperty("data.properties", "email"))
                .password(getProperty("data.properties", "password"))
                .build();
        Response response = requestRegLogin(user, LOGIN_PATH);
        //TokenDto tokenDto = response.as(TokenDto.class);
                softAssert.assertEquals(response.getStatusCode(), 200);
                softAssert.assertTrue(response.getBody().print().contains("token"));
                softAssert.assertAll();
    }


    @Test
    public void loginNegative_wrongPassword(){
        UserDtoLombok user = UserDtoLombok.builder()
                .username(getProperty("data.properties", "email"))
                .password("password")
                .build();
        Response response = requestRegLogin(user, LOGIN_PATH);
        ErrorMessageDto message = ErrorMessageDto.builder().build();
        if (response.getStatusCode()==401){
            message = response.as(ErrorMessageDto.class);
        }
        softAssert.assertEquals(response.getStatusCode(), 401);
        softAssert.assertTrue(message.getMessage().toString().equals("Login or Password incorrect"));
        System.out.println("--> "+message.getTimestamp());
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.toString());
        softAssert.assertEquals(message.getTimestamp().split("T")[0], localDate.toString());
        softAssert.assertAll();
    }
//    @Test
//    public void loginPositiveTest(){
//        UserDto user = new UserDto(getProperty("data.properties", "email"),
//                getProperty("data.properties", "password"));
//        Assert.assertEquals(requestLogin(user, LOGIN_PATH).getStatusCode(), 200);
//
//    }

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
