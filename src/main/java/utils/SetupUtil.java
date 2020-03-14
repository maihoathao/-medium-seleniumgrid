/*
@ Demo test for youtube
@ Author : Hoi
@ aug 2019
 */
package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SetupUtil {
    public static WebDriver driver;

    public static DesiredCapabilities SetupBrowser(String browserType){
        if ("chrome".equals(browserType)) {
            System.out.println("Opening Chrome");
            return DesiredCapabilities.chrome();
        } else if ("firefox".equals(browserType)) {
            System.out.println("Opening firefox");
            return DesiredCapabilities.firefox();
        } else if ("IE".equals(browserType)) {
            System.out.println("Opening IE");
            return DesiredCapabilities.internetExplorer();
        }
        System.out.println("Browser:" + browserType + "is invalid, Launching default browser is Firefox");
        return DesiredCapabilities.firefox();
    }
    public static RemoteWebDriver getDriver(String browser, String nodeUrl) throws MalformedURLException {
        return new RemoteWebDriver(new URL(nodeUrl),SetupBrowser(browser));
    }

    public static void explicitlyWait(WebDriver driver, By byObj){
        WebDriverWait wait = new WebDriverWait(driver,90);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(byObj));
//            wait.until(ExpectedConditions.presenceOfElementLocated(byObj));
        }
        catch (Exception ex){
            System.out.println("Can see object after trying ");
        }

    }
    public static void implicitlyWait(){
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

}
