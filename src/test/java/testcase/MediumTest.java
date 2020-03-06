/*
@ Demo test for youtube
@ Author : Hoi
@ aug 2019
 */
package testcase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.converters.ExtentHtmlReporterConverter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.ArticlePage;
import pages.LoginPage;
import utils.ScreenShotUtil;
import utils.SetupUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MediumTest {
    public static WebDriver driver;
    Properties props;
    ExtentHtmlReporter reporter;
    ExtentReports extent;
    ExtentTest logger;

    @BeforeClass(alwaysRun = true)
    // Call test driver of Grid with browsers setting on TestNG
    @Parameters({"browser","nodeUrl"})
    public void setUp(String browser, String nodeUrl) throws MalformedURLException {
        System.out.println("----START-----");
        driver = SetupUtil.getDriver(browser,nodeUrl);
    }

    @BeforeTest // must to use BeforeTest to capture all @Test -> if use BeforeMethod report capture only @test finally
    public void Report(){
        reporter = new ExtentHtmlReporter("./reports/test-report.html");
//        ExtentHtmlReporter reporter = new ExtentHtmlReporter(System.getProperty("user.home") + "/Projects/medium-seleniumgrid/reports/test-Report.html");
//        reporter.config().setAutoCreateRelativePathMedia(true);
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @Test (priority = 1)
    public void loginMedium() throws MalformedURLException{
        logger = extent.createTest("Testcase 1: Test Login");
        try {
            props = new Properties();
            InputStream file = getClass().getResourceAsStream("/config.properties");
            props.load(file);
            String getUrl = props.getProperty("url");
            driver.navigate().to(getUrl);
            System.out.println("Login to:" + getUrl);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            if (driver.findElements(LoginPage.avatar).size() > 0) {
                System.out.println("---> Login is available ");
            }
            else {
                System.out.println("START LOGIN");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();",driver.findElement(LoginPage.signIn));

                SetupUtil.explicitlyWait(driver,LoginPage.loginGoogle);
                js.executeScript("arguments[0].click();", driver.findElement(LoginPage.loginGoogle));

                SetupUtil.explicitlyWait(driver, LoginPage.emailGoogle);
                String email = props.getProperty("email");
                driver.findElement(LoginPage.emailGoogle).sendKeys(email);
                driver.findElement(LoginPage.emailNext).click();

                SetupUtil.explicitlyWait(driver, LoginPage.password);
                String password = props.getProperty("password");
                driver.findElement(LoginPage.password).sendKeys(password);
                driver.findElement(LoginPage.passwordNext).click();

                SetupUtil.explicitlyWait(driver, LoginPage.avatar);
                WebElement elm = driver.findElement(LoginPage.avatar);
                String nickname = elm.findElement(By.tagName("img")).getAttribute("alt");
                System.out.println(nickname);

                Assert.assertEquals(nickname,props.getProperty("nick"));
                System.out.println("---> Login successful");

            }
        }catch (Exception e){
            System.out.println(e);
            ScreenShotUtil.capture(driver);
            logger.log(Status.FAIL, "open article fail");
        }

    }
    @Test (priority = 2)
    public void articleTest() throws MalformedURLException{
        logger = extent.createTest("Testcase 2: Test open article");
        try{
            SetupUtil.explicitlyWait(driver, ArticlePage.classAll);
//            System.out.println(driver.findElement(By.cssSelector("body")).getText());      // to check data response to test
            List<WebElement> articles = driver.findElements(ArticlePage.articleClass);
            System.out.println("Get article list:" + articles.size());

            // Choice the first article and view it
            WebElement article = articles.get(0);
            WebElement H2Article = article.findElement(By.cssSelector("h2")); // get article in h2 tag
            String articleText = H2Article.getText();
            System.out.println("Choose article: " + articleText);
            H2Article.click(); // must click to h2 tag, not execute to get(0)

            WebElement H1Elm = driver.findElement(By.cssSelector("h1"));
            String getArticleText = H1Elm.getText();
            System.out.println("Opened Article: "+ getArticleText);
            Assert.assertEquals(getArticleText,articleText);
            System.out.println("---> Open Article success");

//            // start write a comment
//            SetupUtil.explicitlyWait(driver,ArticlePage.responseView);
//            driver.findElement(ArticlePage.responseView).click();
//
//            SetupUtil.explicitlyWait(driver,ArticlePage.responeWrite);
//            driver.findElement(ArticlePage.responeWrite).click();

        }catch (Exception e){
            System.out.println(e);
            ScreenShotUtil.capture(driver);
            logger.log(Status.FAIL,"open article fail");
        }
    }
    @AfterMethod
    public void getResult(ITestResult result) throws IOException {
        if(result.getStatus()==ITestResult.FAILURE){
//            String temp = ScreenShotUtil.getScreenshot(driver);
            String temp = ScreenShotUtil.capture(driver);
            logger.fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
        }
        extent.flush();
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }
}
