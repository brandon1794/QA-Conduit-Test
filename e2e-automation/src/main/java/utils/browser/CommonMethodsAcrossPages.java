package utils.browser;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.helperMethods.JvmUtil;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;

// This class is created with the reason of having all those methods used by Selenium so we can have
// a better standard and reuse code
// At the same time here we initialize the driver for chrome, if it's required it can be modified to
// handle n different type of browsers
@Log4j2
public class CommonMethodsAcrossPages {
  private static final Logger LOGGER = LogManager.getLogger(CommonMethodsAcrossPages.class);

  public WebDriver driver;
  private static final Integer WAIT_TIMER = 3000;
  private final Map<String, String> vars = new HashMap<>();
  public Actions actions;

  private final int MAX_TIMEOUT = 60;

  private Pattern p;
  private Matcher m;

  /** The url that an automated test will be testing. */
  private String baseUrl;

  public CommonMethodsAcrossPages() {
    setup();
  }

  private void setup() {

    // Set the webdriver env vars.
    if (JvmUtil.getJvmProperty("os.name").toLowerCase().contains("mac")) {
      System.setProperty("webdriver.chrome.driver", findFile("chromedriver.mac"));
    } else if (JvmUtil.getJvmProperty("os.name").toLowerCase().contains("nix")
        || JvmUtil.getJvmProperty("os.name").toLowerCase().contains("nux")
        || JvmUtil.getJvmProperty("os.name").toLowerCase().contains("aix")) {
      System.setProperty("webdriver.chrome.driver", findFile("chromedriver.linux"));
    } else if (JvmUtil.getJvmProperty("os.name").toLowerCase().contains("win")) {
      System.setProperty("webdriver.chrome.driver", findFile("chromedriver.exe"));
      System.setProperty("webdriver.ie.driver", findFile("iedriver.exe"));
    }

    Capabilities capabilities = DesiredCapabilities.chrome();
    this.driver = new ChromeDriver(capabilities);
  }

  private static String findFile(String filename) {
    String[] paths = {
      "", "bin/", "target/classes"
    }; // if you have chromedriver somewhere else on the path, then put it here.
    for (String path : paths) {
      if (new File(path + filename).exists()) {
        return path + filename;
      }
    }
    return "";
  }

  public void teardown() {
    logInfo("Quitting driver");
    driver.close();
    driver.quit();
  }

  /**
   * Method that acts as an arbiter of implicit timeouts of sorts.. sort of like a Wait For Ajax
   * method.
   */
  private WebElement waitForElement(By by) {
    int attempts = 0;
    int size = this.driver.findElements(by).size();

    while (size == 0) {
      size = this.driver.findElements(by).size();
      // max seconds before failing a script.
      int MAX_ATTEMPTS = 30;
      if (attempts == MAX_ATTEMPTS) {
        fail(String.format("Could not find %s after %d seconds", by.toString(), MAX_ATTEMPTS));
      }
      attempts++;
      try {
        Thread.sleep(WAIT_TIMER); // sleep for 3 second.
      } catch (Exception x) {
        logInfo("There was an error with the wait for an element condition: " + x);
        fail("Failed due to an exception during Thread.sleep!");
      }
    }

    if (size > 1) {
      System.err.println("WARN: There are more than 1 " + by.toString() + " 's!");
    }

    return this.driver.findElement(by);
  }

  /**
   * Wait for a specific condition (polling every 5s, for MAX_TIMEOUT seconds)
   *
   * @param conditions the condition to wait for
   * @return The implementing class for fluency
   */
  public CommonMethodsAcrossPages waitForCondition(ExpectedCondition<?>... conditions) {
    return waitForCondition(MAX_TIMEOUT, conditions);
  }

  /**
   * Wait for a specific condition (polling every 5s)
   *
   * @param conditions the condition to wait for
   * @param timeOutInSeconds the timeout in seconds
   * @return The implementing class for fluency
   */
  public CommonMethodsAcrossPages waitForCondition(
      long timeOutInSeconds, ExpectedCondition<?>... conditions) {
    return waitForCondition(timeOutInSeconds, WAIT_TIMER, conditions); // poll every second
  }

  public CommonMethodsAcrossPages waitForCondition(
      long timeOutInSeconds, long sleepInMillis, ExpectedCondition<?>... conditions) {
    WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds, sleepInMillis);
    Arrays.asList(conditions).forEach(wait::until);
    return this;
  }

  /**
   * Click method that handles the normal click from Selenium This also adds a condition to make it
   * wait some time and search the element to click
   *
   * @param by the type of element we're specting
   * @return The implementing class for fluency
   */
  public CommonMethodsAcrossPages click(By by) {
    waitForCondition(ExpectedConditions.not(ExpectedConditions.invisibilityOfElementLocated(by)))
        .waitForCondition(ExpectedConditions.elementToBeClickable(by));
    waitForElement(by).click();
    return this;
  }

  /**
   * SetText method that handles the clear of a field and also the sendKeys to pass a value to a
   * field This also adds a condition to make it wait some time and search the element to fill
   *
   * @param by the type of element we're specting
   * @param text the value tha we want to pass to field
   * @return The implementing class for fluency
   */
  public CommonMethodsAcrossPages setText(By by, String text) {
    waitForCondition(ExpectedConditions.not(ExpectedConditions.invisibilityOfElementLocated(by)))
        .waitForCondition(ExpectedConditions.elementToBeClickable(by));
    WebElement element = waitForElement(by);
    element.clear();
    element.sendKeys(text);
    return this;
  }

  /**
   * setText method that handles the send keys by passing the element, but this one hits the
   * keyboard tabs This also adds a condition to make it wait some time and search the element
   *
   * @param by the type of element we're specting
   * @param tab the type of key from KeyBoard that needs to be clicked
   * @return The implementing class for fluency
   */
  public CommonMethodsAcrossPages setTextWithExtraInfo(By by, String text, Keys tab) {
    waitForCondition(ExpectedConditions.not(ExpectedConditions.invisibilityOfElementLocated(by)))
        .waitForCondition(ExpectedConditions.elementToBeClickable(by));
    WebElement element = waitForElement(by);
    element.clear();
    element.sendKeys(text);
    element.sendKeys(tab);
    return this;
  }

  /**
   * Logs that are in charge of throwing, recording, adding any info that's necessary to track
   * errors, info or any personal message required
   */
  public CommonMethodsAcrossPages log(Object object) {
    return logInfo(object);
  }

  public CommonMethodsAcrossPages logInfo(Object object) {
    LOGGER.info(object);
    return this;
  }

  /**
   * navigateTo method that handles the navigation to any page
   *
   * @param url the url that's required to navigate
   * @return The implementing class for fluency
   */
  public CommonMethodsAcrossPages navigateTo(String url) {
    // absolute url
    if (url.contains("://")) {
      driver.navigate().to(url);
    } else if (url.startsWith("/")) {
      driver.navigate().to(baseUrl.concat(url));
    } else {
      driver.navigate().to(driver.getCurrentUrl().concat(url));
    }

    return this;
  }

  /**
   * isDisplayed method that handles the validation in the page to check if the element is being
   * displayed
   *
   * @param by the type of element we're specting
   */
  public boolean isDisplayed(By by) {
    return driver.findElement(by).isDisplayed();
  }

  public void maximizeWindow() {
    this.driver.manage().window().maximize();
  }
}
