package utils.helperMethods;

public class JvmUtil {

  /**
   * Get a Jvm property / environment variable
   *
   * @param prop the property to get
   * @return the property value
   */
  public static String getJvmProperty(String prop) {
    return (System.getProperty(prop, System.getenv(prop)));
  }
}
