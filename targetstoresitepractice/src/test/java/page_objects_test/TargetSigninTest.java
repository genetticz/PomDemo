package page_objects_test;

import application_page_base.ApplicationPageBase;
import base.BrowserDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import page_objects.TargetHome;
import page_objects.TargetSignin;
import reader.MyDataReader;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class TargetSigninTest extends BrowserDriver {

    TargetHome t1;
    TargetSignin ts1;

    @BeforeMethod
    public void init(){
        t1 = PageFactory.initElements(driver,TargetHome.class);
        ts1 = PageFactory.initElements(driver,TargetSignin.class);
    }

    @Test(priority = 0)
    public void testSignIn() {

        boolean signInUrl = false;
        t1.getSignOn();
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.elementToBeClickable(t1.isSignOn()));
        t1.clickSignOn();
        if (driver.getCurrentUrl().contains("login"))
            signInUrl = true;
        Assert.assertTrue(signInUrl);
    }

    @DataProvider
    public Object[][] retrieveSheets() throws Exception {
        MyDataReader excelData = new MyDataReader("../targetstoresitepractice/testData/testusers.xlsx");
        //excelData.setExcelFile(System.getProperty("user.dir") + "/targetstoresitepractice/testData/testusers.xlsx");
//        String[][] usersData = excelData.getExcelSheetData("Sheet1");
        String[][] usersData = excelData.getExcelSheetData(chosenSheet);
        return usersData;
    }

    @Test(dataProvider = "retrieveSheets", priority = 1)
    public void signIn(String username,String password) throws InterruptedException {

        t1.getSignOn();
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.elementToBeClickable(t1.isSignOn()));
        t1.clickSignOn();
        ts1.getUsername(username);
        ts1.getUserpass(password);
        ts1.clickSignin();
    }
}