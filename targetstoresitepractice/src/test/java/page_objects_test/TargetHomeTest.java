package page_objects_test;

import base.BrowserDriver;
import com.relevantcodes.extentreports.LogStatus;
import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page_objects.TargetHome;
import reporting.ExtentTestManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class TargetHomeTest extends BrowserDriver {

    TargetHome t1;

    @BeforeMethod
    public void init(){
        t1 = PageFactory.initElements(driver,TargetHome.class);
    }
    @Test(priority = 2)
    public void testRetrieveCategories() {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        List<String> homeLinks = t1.retrieveCategories();
//        homeLinks.add(1,"https://www.target.com/knkhhkhkhkh");
        boolean notBroken = true;
        for(int i = 0; i < homeLinks.size(); i++) {
            driver.get(homeLinks.get(i));
            if(t1.doesItExist()) {
                notBroken = false;
                Reporter.log("Broken link: " + homeLinks.get(i),true);
            }
        }
        Assert.assertTrue(notBroken);
    }

    @Test(priority = 0)
    public void testSearch() {
        t1.searchProduct();
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.visibilityOf(t1.isSearch()));
        String expected = "Shirt : Target";
        String actual  = driver.getTitle();
        Reporter.log(actual,true);
        Assert.assertEquals(expected,actual);
    }

    @Test(priority = 1)
    public void getSignIn()  {
        t1.getSignOn();
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.visibilityOf(t1.isSignOn()));
        t1.clickSignOn();
    }
}