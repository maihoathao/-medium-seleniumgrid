package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShotUtil {

    // method 1 : use after Catch method
    public static String capture(WebDriver driver){
        byte[] buffer = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        StringBuffer failpicPath = new StringBuffer();
//        failpicPath.append(System.getProperty("user.home") + "/Projects/medium-seleniumgrid/screenshot/");
        failpicPath.append("./screenshot/");
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
        System.out.println("Image is saved:" + failpicPath.toString());
        return fn;
    }


//    //method 2 :
//    public static String getScreenshot(WebDriver driver)
//    {
//        TakesScreenshot ts=(TakesScreenshot) driver;
//
//        File src=ts.getScreenshotAs(OutputType.FILE);
//
//        String path=System.getProperty("/Users/maihoathao/Projects/medium-seleniumgrid") + "/Screenshot/" + System.currentTimeMillis()+ ".png";
//
//        File destination=new File(path);
//
//        try
//        {
//            FileUtils.copyFile(src, destination);
//        } catch (IOException e)
//        {
//            System.out.println("Capture Failed "+e.getMessage());
//        }
//
//        return path;
//    }

}
