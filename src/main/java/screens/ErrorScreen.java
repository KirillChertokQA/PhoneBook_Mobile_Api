package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

public class ErrorScreen extends BaseScreen{
    public ErrorScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(id = "android:id/message")
    AndroidElement errorMessage;

    public boolean validateErrorMessage(String text, int time){
        return textInElementPresent(errorMessage, text, time);
    }
}
