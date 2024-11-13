package helper;

import dto.TokenDto;
import dto.UserDtoLombok;
import interfaces.BaseApi;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static helper.PropertiesReader.getProperty;
import static io.restassured.RestAssured.given;

public class HelperApiMobile implements BaseApi {

    TokenDto tokenDto;

    public void login(String email, String password) {
        UserDtoLombok user = UserDtoLombok.builder()
                .username(email)
                .password(password)
                .build();
        Response response = given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL + LOGIN_PATH)
                .thenReturn();
        if (response.getStatusCode() == 200) {
            tokenDto = response.as(TokenDto.class);
        }else {
            System.out.println("Something went wrong, status code -->"+response.getStatusCode());
        }
    }

    public Response getUserContactsResponse(){
        return given()
                .header("Authorization", tokenDto.getToken())
                .when()
                .get(BASE_URL+GET_ALL_CONTACTS_PATH)
                .thenReturn();

    }

}
