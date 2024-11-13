package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
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
}
