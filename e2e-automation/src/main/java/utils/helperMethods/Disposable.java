package utils.helperMethods;

import com.mycila.guice.ext.closeable.InjectorCloseListener;
import lombok.SneakyThrows;

// Created this class to handle the dependency injection of the project and also to be able to
// handle the instances of the classes
public interface Disposable extends AutoCloseable, InjectorCloseListener {
  void dispose();

  @SneakyThrows
  default void onInjectorClosing() {
    this.dispose();
  }

  default void close() throws Exception {
    this.dispose();
  }
}
