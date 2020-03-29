package pages;

import org.openqa.selenium.By;

public class ArticlePage {
    public static final By homeLink = By.cssSelector("a[href=\"https://medium.com/\"]");

    public static final By classAll = By.cssSelector(".extremeHero-smallCardContainer");
    public static final By articleClass = By.cssSelector(".extremeHero-smallCardContainer article");

    public static final By responseView = By.xpath("//*[@id=\"root\"]/div/div[5]/div/div[1]/div/div[6]/a/span/div/span");
//    public static final By responseFirst = By.xpath("//*[@id=\"root\"]/div/div[5]/div/div[1]/div/div[6]/a/span/div/span");
    public static final By responeWrite = By.cssSelector(".inlineEditor-headerContent");

    public static final By listCount = By.cssSelector(".js-postListHandle");
    public static final By articleCount = By.cssSelector(".js-block");
    public static final By h3Article = By.cssSelector("h3"); // title before open detail
    public static final By h1Article = By.cssSelector("h1"); // title after open detail
}
