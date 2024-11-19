package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SplashScreen extends BaseScreen{
    public SplashScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }
    @FindBy(id = "com.sheygam.contactapp:id/version_text")
    AndroidElement versionApp;

    public boolean validateVersion() {
        return textInElementPresent(versionApp, "Version 1.0.0", 5);
    }

    public void goToAuthScreen() {
        pause(5);
    }

    public void goToAuthScreen(int time) {
       try {
           new WebDriverWait(driver, time)
                   .until(ExpectedConditions.presenceOfElementLocated(
                           By.xpath("//*[@text='Authentication']")));
       }catch (org.openqa.selenium.TimeoutException e){
           e.printStackTrace();
           System.out.println("element Authentication not find");
       }
    }

    public boolean validateSplashScreenToDisappear(long expectedTime) {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        pause(4);
        if (new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOf(versionApp)))
            endTime = System.currentTimeMillis();
        long splashDuration = endTime - startTime;
        System.out.println("--> "+splashDuration);
        if(splashDuration <= expectedTime)
        return true;
        else return false;
    }
}