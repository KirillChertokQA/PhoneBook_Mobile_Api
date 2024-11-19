package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ContactsScreen extends BaseScreen{
    public ContactsScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(xpath = "//android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.TextView ")
    AndroidElement headerContactsScreen;

    @FindBy(id = "com.sheygam.contactapp:id/add_contact_btn")
    AndroidElement btnAddNewContact;

    @FindBy(xpath ="/hierarchy/android.widget.Toast")
    AndroidElement popUpMessage;

    @FindBy(xpath = "//*[@resource-id='com.sheygam.contactapp:id/rowContainer']")
    AndroidElement firstElementContactList;

    @FindBy(id ="android:id/button1")
    AndroidElement popUpBtnYes;


    public boolean validateHeader(){
        return textInElementPresent(headerContactsScreen, "Contact list", 5);
    }

    public void clickBtnAddNewContact(){
       // btnAddNewContact.click();
        clickWait(btnAddNewContact, 5);
    }

    public boolean validatePopUpMessage(){
        return textInElementPresent(popUpMessage, "Contact was added!", 5);
    }

    public void deleteContact(){
        pause(3);
        int xLeftUpCorner = firstElementContactList.getLocation().getX();
        int yLeftUpCorner = firstElementContactList.getLocation().getY();
        int heightElement = firstElementContactList.getSize().getHeight();
        int wightElement = firstElementContactList.getSize().getWidth();
        System.out.println("y --> " +firstElementContactList.getLocation().getY());
        System.out.println("x --> " +firstElementContactList.getLocation().getX());
        System.out.println("h --> " +firstElementContactList.getSize().getHeight());
        System.out.println("w --> " +firstElementContactList.getSize().getWidth());
        TouchAction<?> touchAction = new TouchAction(driver);
        touchAction.longPress(PointOption.point(wightElement/3,(yLeftUpCorner+heightElement/2)))
                .moveTo(PointOption.point(wightElement/6*5, (yLeftUpCorner+heightElement/2)))
                        .release().perform();

    }

    public void clickBtnYes(){
        popUpBtnYes.click();
    }

    public int getContactNumber(){
        pause(3);
        AndroidElement firstElementContactList1 = firstElementContactList;
        return driver.findElements(By.xpath("//*[@resource-id='com.sheygam.contactapp:id/rowContainer']")).size();

    }
}
