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
import org.apache.maven.plugin.surefire.runorder.Priority;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
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
import java.util.logging.Logger;

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
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @Test (priority = 1, description = "Login Medium by Google account")
    public void loginMedium() throws MalformedURLException{
        logger = extent.createTest("Testcase 1: Test Login");
        try {
//            Get data from properties file
            props = new Properties();
            InputStream file = getClass().getResourceAsStream("/config.properties");
            props.load(file);
            String getUrl = props.getProperty("url");
            driver.navigate().to(getUrl);
            System.out.println("Login to:" + getUrl);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

//            Check status of login
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
//            Get data inputted from excel to search article
            sheet = ExcelUtil.readExcel(excelFile,"search_article");
            String textSearch = sheet.getRow(1).getCell(1).getStringCellValue();
            System.out.println("Get text to searchArticle: " + textSearch);

//            Click to search button
            WebElement searchButton = driver.findElement(HomePage.searchBtn);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", searchButton);

//            Click to search button with inputted data
            SetupUtil.explicitlyWait(driver, HomePage.searchForm);
            driver.findElement(HomePage.searchClick).sendKeys(textSearch);
            ScreenShotUtil.capture(driver, "searchArticle-result-");

//            Check search result,get list data, with each item in list, compare with data is inputted
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
            logger.log(Status.FAIL, "Search Article un-success");
        }
    }

    @Test(priority = 3, description = "Open a Article which matching with testcase 2")
    public void openArticle() throws MalformedURLException {
        logger = extent.createTest("Testcase3: Open Article");
        try {
//            In Article list above, Open the first Article
            List<WebElement> countArticles = driver.findElements(ArticlePage.articleCount);
            System.out.println("List Articles: " + countArticles.size());
            WebElement article = countArticles.get(0);
            WebElement getH3Title = article.findElement(ArticlePage.h3Article);
            String h3Title = getH3Title.getText();
            System.out.println("Chosen Article: " + h3Title);
            article.click();
            ScreenShotUtil.capture(driver, "article-detail-");

//            Compare title of article before and after click
            WebElement getH1Title = driver.findElement(ArticlePage.h1Article);
            String h1Title = getH1Title.getText();
            System.out.println("Opened article: " + h1Title);
            Assert.assertEquals(SetupUtil.deAccent(h1Title), SetupUtil.deAccent(h3Title));// using deAccent to covert regex characters.
            System.out.println("---> Open Article success");

        } catch (Exception e) {
            System.out.println(e);
            ScreenShotUtil.capture(driver,"openArticle-error-");
            logger.log(Status.FAIL, "Open detail article is error");
        }
    }

    @Test (priority = 4, description = "Bookmark Article")
    public void bookmarkArticle() throws MalformedURLException {
        logger = extent.createTest("Testcase 4: Bookmark Article");
        try {
//            Get list action perform with Article
//            Because this page generated class auto so need get list action have the same tag, and then select follow item position
            List<WebElement> actionsGroup = driver.findElements(ArticlePage.actionsGroup);
            System.out.println("actions list: " + actionsGroup);
            WebElement childBookmark = actionsGroup.get(3);
            WebElement parentBookmark = (WebElement)((JavascriptExecutor) driver).executeScript("return arguments[0].parentNode;",childBookmark);
            parentBookmark.click();
            ScreenShotUtil.capture(driver, " bookmark-");

//           Need re-get list action because each times refresh page will be auto generated new class
            List<WebElement> actionGroupReGet = driver.findElements(ArticlePage.actionsGroup);
            WebElement childBookmarkReGet= actionGroupReGet.get(3);
            WebElement childPath = childBookmarkReGet.findElement(ArticlePage.childPath);

//            if (childPath.getAttribute("fill-rule").isEmpty()){
//                System.out.println("bookmark is NG");
//            } else {
//                String ruleText = childPath.getCssValue("fill-rule");
//                System.out.println("Bookmark is ok" + ruleText);
//            }


            WebElement  dataPath = childPath.findElement(ArticlePage.rulePath);
            String contentRule = dataPath.getAttribute("fill-rule");
            if (contentRule != null){
                System.out.println("Bookmark is OK, with content is: " + contentRule);
            }
            else {
                System.out.println("bookmark is NG");
            }


//            Get tooltip, because tooltip existed in parent node of parentBookmark
//            WebElement parentTooltip = (WebElement)((JavascriptExecutor) driver).executeScript("return arguments[0].parentNode;", parentBookmark);
//            Actions action = new Actions(driver);
//            action.moveToElement(parentTooltip).perform();
//            String textTooltip = parentTooltip.getText();
//            System.out.println(textTooltip);


        } catch (Exception e) {
            System.out.println(e);
            ScreenShotUtil.capture(driver,"bookmark-error-");
            logger.log(Status.FAIL, "Bookmark article is error");
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
