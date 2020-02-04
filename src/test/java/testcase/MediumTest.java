/*
@ Demo test for youtube
@ Author : Hoi
@ aug 2019
 */
package testcase;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.MediumPages;
import utils.SetupUtil;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.util.Properties;

public class MediumTest {
    public static WebDriver driver;
    Properties props;

    @BeforeClass(alwaysRun = true)

    // Call test driver of Grid with browsers setting on testng
    @Parameters({"browser","nodeUrl"})
    public void setUp(String browser, String nodeUrl) throws MalformedURLException {
        System.out.println("----START-----");
        driver = SetupUtil.getDriver(browser,nodeUrl);
    }

    // test login to git
    @Test(priority = 1)
    public void loginMedium() throws MalformedURLException{
        try {
            props = new Properties();
            FileInputStream file = new FileInputStream("/Users/maihoathao/Projects/youtube-seleniumgrid/src/main/resources/config.properties");
            props.load(file);
            String getUrl = props.getProperty("url");
//            String getEmail = props.getProperty("email");
//            String getPass = props.getProperty("password");
            driver.navigate().to(getUrl);
            System.out.println("Login to:" + getUrl);
            Thread.sleep(30);
//            SetupUtil.implicitlyWait();

            if (driver.findElement(MediumPages.avatar).isEnabled()){
                System.out.println("---> Login is available ");
            }
            else {
                System.out.println("START LOGIN");
                driver.findElement(MediumPages.signin).click();
                driver.findElement(MediumPages.loginGoogle).click();
                SetupUtil.explicitlyWait(driver, MediumPages.avatar);
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }

}
