package pages;

import org.openqa.selenium.By;

public class ArticlePage {
    public static final By classAll = By.cssSelector(".extremeHero-smallCardContainer");
    public static final By articleClass = By.cssSelector(".extremeHero-smallCardContainer article");

    public static final By responseView = By.xpath("//*[@id=\"root\"]/div/div[5]/div/div[1]/div/div[6]/a/span/div/span");
//    public static final By responseFirst = By.xpath("//*[@id=\"root\"]/div/div[5]/div/div[1]/div/div[6]/a/span/div/span");
    public static final By responeWrite = By.cssSelector(".inlineEditor-headerContent");

}
