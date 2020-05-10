package page_objects;

import application_page_base.ApplicationPageBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class TargetSignin {

    private WebDriver driver;

    @FindBy(how = How.ID, id = "username")
    WebElement username;

    @FindBy(how = How.ID, id = "password")
    WebElement userpass;

    @FindBy(how = How.ID, id = "login")
    WebElement signBtn;

    public void getUsername(String user){
        ApplicationPageBase.sendKeys("username",username,user);
    }
    public void getUserpass(String pass){
        ApplicationPageBase.sendKeys("password",userpass,pass);
    }
    public void clickSignin(){
        signBtn.click();
    }
}
