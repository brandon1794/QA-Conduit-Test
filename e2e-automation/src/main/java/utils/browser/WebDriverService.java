package utils.browser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import utils.helperMethods.Disposable;

/** Created by Brandon. */

// Created this class to handle the instance of the CommonPage that has all methods and driver and
// also to close the driver at the end of every test
@Log4j2
@Singleton
public class WebDriverService implements Disposable {

  private CommonMethodsAcrossPages webDriver;

  @Inject
  public CommonMethodsAcrossPages getDriver() {
    if (this.webDriver == null) {
      this.webDriver = new CommonMethodsAcrossPages();
    }

    return webDriver;
  }

  @Override
  public void dispose() {
    if (this.webDriver != null) {
      this.webDriver.teardown();
    }
  }
}
