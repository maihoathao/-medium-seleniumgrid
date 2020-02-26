package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotsUtil {
    static WebDriver driver;
//    public static void screenShot(ITestResult result){ // Using ITestResult of TestNG to capture failure image
//        if(ITestResult.FAILURE==result.getStatus()){
//            try{
//                TakesScreenshot screenshot = (TakesScreenshot) driver;
//                File src = screenshot.getScreenshotAs(OutputType.FILE);
//                FileUtils.copyFile(src, new File("/Users/maihoathao/Projects/youtube-seleniumgrid/screenshot" + result.getName() + ".png"));
//            } catch (Exception e){
//                System.out.println(e);
//            }
//        }
//    }
    public static void capture(WebDriver driver){
        byte[] buffer = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        StringBuffer failpicPath = new StringBuffer();
        failpicPath.append("/Users/maihoathao/Projects/medium-seleniumgrid/screenshot/");
        String fn = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString();
        failpicPath.append(fn);
        failpicPath.append(".png");
        File file = new File(failpicPath.toString());
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(buffer);
            fo.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("file saved:" + failpicPath.toString());
        System.out.println("Save screenshot is ok");
    }


}
