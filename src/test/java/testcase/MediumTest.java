/*
@ Demo test for youtube
@ Author : Hoi
@ aug 2019
 */
package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.ArticlePage;
import pages.LoginPage;
import utils.SetupUtil;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
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

    @Test (priority = 1)
    public void loginMedium() throws MalformedURLException{
        try {
            props = new Properties();
            InputStream file = getClass().getResourceAsStream("/config.properties");
            props.load(file);
            String getUrl = props.getProperty("url");
            driver.navigate().to(getUrl);
            System.out.println("Login to:" + getUrl);
            Thread.sleep(30);
            if (driver.findElements(LoginPage.avatar).size() > 0) {
                System.out.println("---> Login is available ");
            }
            else {
                System.out.println("START LOGIN");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();",driver.findElement(LoginPage.signIn));
//                driver.findElement(LoginPage.signIn).click();

                SetupUtil.explicitlyWait(driver,LoginPage.loginGoogle);
                js.executeScript("arguments[0].click();", driver.findElement(LoginPage.loginGoogle));
//                driver.findElement(LoginPage.loginGoogle).click();

                SetupUtil.explicitlyWait(driver, LoginPage.emailGoogle);
                String email = props.getProperty("email");
                driver.findElement(LoginPage.emailGoogle).sendKeys(email);
                driver.findElement(LoginPage.emailNext).click();

                SetupUtil.explicitlyWait(driver, LoginPage.password);
                String password = props.getProperty("password");
                driver.findElement(LoginPage.password).sendKeys(password);
                driver.findElement(LoginPage.passwordNext).click();

                SetupUtil.explicitlyWait(driver, LoginPage.avatar);
                if (driver.findElements(LoginPage.avatar).size() > 0) {
                    System.out.println("Login successful");
                } else {
                    System.out.println("Login error");
                }

            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    @Test (priority = 2)
    public void articleTest() throws MalformedURLException{
        try{
//            List<WebElement> articles = driver.findElements(ArticlePage.articleClass);
//            int count = articles.size();
//            System.out.println("Get article list:" + count + articles);
//            JavascriptExecutor js = (JavascriptExecutor) driver;
            // List<WebElement> articles = (List<WebElement>) js.executeScript("return document.querySelectorAll('.extremeHero-smallCardContainer article');");
            // Get list articles
            SetupUtil.explicitlyWait(driver, ArticlePage.classAll);
            System.out.println(driver.findElement(By.cssSelector("body")).getText());      // to check data response to test
            List<WebElement> articles = driver.findElements(ArticlePage.articleClass);
            System.out.println("Get article list:" + articles.size());

            // Choice the first article and view it
            WebElement article = articles.get(0);
            System.out.println("Selected article:" + article.getText());
            article.click();
            System.out.println("Open Article success");

//            // start write a comment
//            SetupUtil.explicitlyWait(driver,ArticlePage.responseView);
//            driver.findElement(ArticlePage.responseView).click();
//
//            SetupUtil.explicitlyWait(driver,ArticlePage.responeWrite);
//            driver.findElement(ArticlePage.responeWrite).click();

            
            

        }catch (Exception e){
            System.out.println(e);
        }

    }

}
