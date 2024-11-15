package config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;

public class AppiumConfig {

    public static AppiumDriver<AndroidElement> driver;
    public int height = 0, wight = 0;

   // "platformName": "Android",
    // "deviceName": "Nex4",
    // "platformVersion": "8.0",
    // "appPackage": "com.sheygam.contactapp",
    // "appActivity": ".SplashActivity"

    @BeforeMethod
    public void setup(){
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "Nexus_4_API_26");
        desiredCapabilities.setCapability("platformVersion", "8.0");
        desiredCapabilities.setCapability("appPackage", "com.sheygam.contactapp");
        desiredCapabilities.setCapability("appActivity", ".SplashActivity");

        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
         try {
            driver = new AppiumDriver<>(new URL("http://localhost:4723/wd/hub"), desiredCapabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

         height = driver.manage().window().getSize().getHeight();
         wight = driver.manage().window().getSize().getWidth();
        System.out.println(wight+ "X"+ height);

    }

    @AfterMethod
    public void tearDown(){
        //driver.quit();

    }
}
