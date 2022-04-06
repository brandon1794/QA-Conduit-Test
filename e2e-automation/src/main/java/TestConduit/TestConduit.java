package TestConduit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.browser.WebDriverService;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*This class is in charge of selecting all the elements from the web page, also to create the methods that are required to do
clicks, send text, dropdowns, etc etc. Try to reuse code as much as possible and use the methods that are coming from
the class of CommonMethods, they are a wrapper of selenium. */
@Singleton
public class TestConduit {
  private final WebDriverService webDriverService;
  private static final Random random = new Random();
  private static final String email = getRandomEmail();

  @Inject
  public TestConduit(WebDriverService webDriverService) {
    this.webDriverService = webDriverService;
  }

  public void navigateToTestConduitPage(String url) {
    this.webDriverService.getDriver().maximizeWindow();
    this.webDriverService.getDriver().navigateTo(url);
    this.webDriverService
        .getDriver()
        .waitForCondition(
            ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".styles_content-welcome__1NZX2 > h1"), "conduit"));
  }

  public void clickToSignUpAndRegisterAccount() {
    this.webDriverService
        .getDriver()
        .logInfo("Clicking sign up and register link")
        .click(By.xpath("//span[contains(text(), \"Sign Up\")]"));
  }

  public void clickToLoginIntoAccount() {
    this.webDriverService
        .getDriver()
        .logInfo("Clicking sign in account link")
        .click(By.xpath("//span[contains(text(), \"Sign In\")]"));
  }

  public void fillSignUpForm(String password) {
    String userName = randomUserName();

    this.webDriverService
        .getDriver()
        .waitForCondition(
            ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h1"), "Sign Up"));

    this.webDriverService
        .getDriver()
        .logInfo("Typing user name: " + userName)
        .setText(By.id("username"), userName)
        .logInfo("Typing email: " + email)
        .setText(By.id("email"), email)
        .logInfo("Typing password: " + password)
        .setText(By.id("password"), password)
        .logInfo("Clicking sign up button")
        .click(By.className("custom-btn"));
  }

  public void loginForm(String password) {
    this.webDriverService
        .getDriver()
        .waitForCondition(
            ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h1"), "Sign In"));

    this.webDriverService
        .getDriver()
        .logInfo("Typing email: " + email)
        .setText(By.id("email"), email)
        .logInfo("Typing password: " + password)
        .setText(By.id("password"), password)
        .logInfo("Clicking login button")
        .click(By.className("custom-btn"));
  }

  public void isProfileDisplayed() {
    this.webDriverService
        .getDriver()
        .waitForCondition(
            ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".styles_content-welcome__1NZX2 > h1"), "conduit"));

    this.webDriverService
        .getDriver()
        .isDisplayed(By.xpath("//span[contains(text(), \"Profile\")]"));
  }

  public void isNewPostDisplayed() {
    this.webDriverService
        .getDriver()
        .waitForCondition(
            ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".styles_content-welcome__1NZX2 > h1"), "conduit"));
    this.webDriverService
        .getDriver()
        .isDisplayed(By.xpath("//span[contains(text(), \"New Post\")]"));
  }

  public void clickToNewPost() {

    this.webDriverService
        .getDriver()
        .click(By.xpath("//span[contains(text(), \"New Post\")]"));
  }

  public void addNewPost() {
    this.webDriverService
        .getDriver()
        .waitForCondition(
            ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h1"), "New Post"));

    this.webDriverService
        .getDriver()
        .setText(By.id("title"), "Testing Brandon Post")
        .setText(By.id("description"), "Just a Random Test")
        .setText(By.className("ql-editor"), "Brandon Random Test Article")
        .setTextWithExtraInfo(By.id("tagList"), "Test", Keys.ENTER)
        .click(By.className("custom-btn"));
  }

  public void newPostAdded() {
    this.webDriverService
        .getDriver()
        .waitForCondition(
            ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("h1"), "Testing Brandon Post"));
  }

  private static String getRandomEmail() {
    int length = 4;

    return "bran"
        + IntStream.range(0, length)
            .mapToObj(i -> String.valueOf((char) ('A' + random.nextInt(26))))
            .collect(Collectors.joining())
            .concat("_test")
            .concat("@mailinator.com");
  }

  private static String randomUserName() {
    int length = 4;

    return "TestingUser"
        + IntStream.range(0, length)
            .mapToObj(i -> String.valueOf((char) ('A' + random.nextInt(26))))
            .collect(Collectors.joining());
  }
}
