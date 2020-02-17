package pages;

import org.openqa.selenium.By;

public class LoginPage {

    public static final By signIn = By.xpath("//a[text()=\"Sign in\"]");
    public static final By loginGoogle = By.cssSelector("#susi-modal-google-button a"); // By.id("susi-modal-google-button");
    public static final By emailGoogle = By.cssSelector("#identifierId");

    public static final By emailNext = By.id("identifierNext");
    public static final By password = By.cssSelector("input[type=\"password\"]");
    public static final By passwordNext = By.id("passwordNext");
    public static final By avatar = By.className("avatar");


}