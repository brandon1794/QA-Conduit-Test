package config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mycila.guice.ext.closeable.CloseableInjector;
import com.mycila.guice.ext.closeable.CloseableModule;
import cucumber.api.java.ObjectFactory;

// This class is created in order to create just 1 instance of the classes, by doing these we avoid
// the creation of n drivers,
// so we will have 1 driver opened that will execute all the scenarios
public class GuiceFactory implements ObjectFactory {
  private final Module[] modules = {new CloseableModule()};
  private Injector injector = null;

  @Override
  public void start() {
    this.injector = Guice.createInjector(modules);
  }

  @Override
  public void stop() {
    this.injector.getInstance(CloseableInjector.class).close();
    this.injector = null;
  }

  @Override
  public boolean addClass(Class<?> glueClass) {
    return true;
  }

  @Override
  public <T> T getInstance(Class<T> glueClass) {
    return this.injector.getInstance(glueClass);
  }
}
