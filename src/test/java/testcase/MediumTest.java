/*
@ Demo test for Medium
@ Author : Hoi
@ aug 2019
 */
package testcase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.ArticlePage;
import pages.HomePage;
import utils.ExcelUtil;
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
    public static XSSFWorkbook wb;
    public static XSSFSheet sheet;
    public static XSSFCell cell;
    Properties props;
    ExtentHtmlReporter reporter;
    ExtentReports extent;
    ExtentTest logger;
    String excelFile = "/Users/maihoathao/Projects/medium-seleniumgrid/src/main/resources/testdata.xlsx";

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
//        reporter.config().setAutoCreateRelativePathMedia(true);
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @Test (priority = 1, description = "Login Medium by Google account")
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


            if (driver.findElements(HomePage.avatar).size() > 0) {
                System.out.println("---> Login is available ");
            }
            else {
                System.out.println("START LOGIN");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();",driver.findElement(HomePage.signIn));

                SetupUtil.explicitlyWait(driver, HomePage.loginGoogle);
                js.executeScript("arguments[0].click();", driver.findElement(HomePage.loginGoogle));

                SetupUtil.explicitlyWait(driver, HomePage.emailGoogle);
                String email = props.getProperty("email");
                driver.findElement(HomePage.emailGoogle).sendKeys(email);
                driver.findElement(HomePage.emailNext).click();

                SetupUtil.explicitlyWait(driver, HomePage.password);
                String password = props.getProperty("password");
                driver.findElement(HomePage.password).sendKeys(password);
                driver.findElement(HomePage.passwordNext).click();

                SetupUtil.explicitlyWait(driver, HomePage.avatar);
                WebElement elm = driver.findElement(HomePage.avatar);
                String nickname = elm.findElement(By.tagName("img")).getAttribute("alt");
                System.out.println(nickname);

                Assert.assertEquals(nickname,props.getProperty("nick"));
                System.out.println("---> Login successful");
            }
        } catch (Exception e) {
            System.out.println(e);
            ScreenShotUtil.capture(driver,"login-error-");
            logger.log(Status.FAIL, "open article fail");
        }
    }

    @Test (priority = 2, description = " Input and searchArticle data input from excel")
    public void searchArticle() throws MalformedURLException{
        logger = extent.createTest("Testcase2 : Search Article");
        try {
            sheet = ExcelUtil.readExcel(excelFile,"search_article");
            String textSearch = sheet.getRow(1).getCell(1).getStringCellValue();
            System.out.println("Get text to searchArticle: " + textSearch);

            WebElement searchButton = driver.findElement(HomePage.searchBtn);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", searchButton);

            SetupUtil.explicitlyWait(driver, HomePage.searchForm);
            driver.findElement(HomePage.searchClick).sendKeys(textSearch);
            ScreenShotUtil.capture(driver, "searchArticle-result-");

            SetupUtil.explicitlyWait(driver,ArticlePage.listCount);
            WebElement postList = driver.findElement(ArticlePage.listCount);
            List<WebElement> listArticles = postList.findElements(ArticlePage.articleCount);

            boolean checkList = true;
            for (WebElement getArticle : listArticles){
                WebElement getTitle = getArticle.findElement(ArticlePage.h3Article);
                String titleArticle = getTitle.getText();
                String title = titleArticle.toLowerCase();
                if (title.contains(textSearch)) {
                    System.out.println("Item is ok: [" + textSearch + "]: " + title);
                } else {
                    System.out.println("Item is NOT ok: [" + textSearch + "]: " + title);
                    checkList = false;
                }
            }
            Assert.assertTrue(checkList, "List is not match --> ");
        } catch (Exception e) {
            System.out.println(e);
            ScreenShotUtil.capture(driver,"searchArticle-error-");
        }
    }
    @Test(priority = 3, description = "Open a Article which matching with testcase 2")
    public void openArticle() throws MalformedURLException {
        logger = extent.createTest("Testcase3: Open Article");
        try {
            List<WebElement> countArticles = driver.findElements(ArticlePage.articleCount);
            System.out.println("List Articles: " + countArticles.size());
            WebElement article = countArticles.get(0);
            WebElement getH3Title = article.findElement(ArticlePage.h3Article);
            String h3Title = getH3Title.getText();
            System.out.println("H3 titile: " + h3Title);
            article.click();
            ScreenShotUtil.capture(driver, "article-detail-");

            WebElement getH1Title = driver.findElement(ArticlePage.h1Article);
            String h1Title = getH1Title.getText();
            System.out.println("H1 title: " + h1Title);
            Assert.assertEquals(SetupUtil.deAccent(h1Title), SetupUtil.deAccent(h3Title));
            System.out.println("---> Open Article success");

        } catch (Exception e) {
            System.out.println(e);
            ScreenShotUtil.capture(driver,"openArticle-error-");
        }
    }

//    @Test (priority = 3, description = "Open a Article in [New from your network] list at Home page")
    public void articleTest() throws MalformedURLException {
//        Back to homepage and choose a article in list
        WebElement home = driver.findElement(ArticlePage.homeLink);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()",home);
        ScreenShotUtil.capture(driver,"homepage-");

        logger = extent.createTest("Testcase 3: Test open article");
        try {
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


        } catch (Exception e) {
            System.out.println(e);
            ScreenShotUtil.capture(driver,"article-detail-error-");
            logger.log(Status.FAIL,"open article fail");
        }
    }





    @AfterMethod
    public void getResult(ITestResult result) throws IOException {
        if (result.getStatus()==ITestResult.FAILURE) {
            String temp = ScreenShotUtil.capture(driver,"error-");
            logger.fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
        }
        extent.flush();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
