package ext.base.runtime;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import ext.base.log.*;


/**
 * Java runtime utility
 */
public final class BcRuntimeUtil extends Object {

  private static int javaMajorVersion;

  /** constructor */
  private BcRuntimeUtil() {
    super();
  }


  /**
   * Returns the user's current working directory.
   *
   * @return File
   */
  public static File getUserDir() {
    File dir = new File(System.getProperty("user.dir"));
    return dir;
  }


  /**
   * Returns the java version.。
   * 
   * @return String
   */
  public static String getJavaVersion() {
    return System.getProperties().getProperty("java.version");
  }


  /**
   * Returns the major version of Java as an integer.<br>
   * 1.0 to 1.8 is 1.
   *
   * @return int
   */
  public static int getJavaMajorVersion() {
    if (javaMajorVersion <= 0) {
      String javaVersion = getJavaVersion();
      int pos = javaVersion.indexOf(".");
      String str = javaVersion.substring(0, pos);
      javaMajorVersion = Integer.parseInt(str);
    }
    return javaMajorVersion;
  }


  /**
   * Add the classpath to the parameter ClassLoader.
   *
   * @param classLoader Collection<File>
   * @param ufileCollection Collection<File>
   * @return ClassLoader
   */
  public static ClassLoader addClassPath(ClassLoader classLoader, Collection<File> fileCollection) throws Exception {
    if (getJavaMajorVersion() >= 9) {
      if (classLoader instanceof java.lang.instrument.Instrumentation) {
        java.lang.instrument.Instrumentation instrumentation = (java.lang.instrument.Instrumentation)classLoader;
        BcLogUtil.debug("instrumentation="+instrumentation);
      }
      
      // Java9 or later
      Method method = classLoader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
      method.setAccessible(true);
      for (File file : fileCollection) {
        method.invoke(classLoader, file.getPath());
      }
      return classLoader;

    } else {
      // Java8
      URLClassLoader urlClassLoader = null;
      if (classLoader instanceof URLClassLoader) {
        urlClassLoader = (URLClassLoader)classLoader;
      } else {
        URL[] urlsDummy = {};
        urlClassLoader = URLClassLoader.newInstance(urlsDummy, classLoader);
      }
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
      method.setAccessible(true);
      for (File file : fileCollection) {
        URL url = file.toURI().toURL();
        method.invoke(urlClassLoader, new Object[] { url });
      }
      return urlClassLoader;
    }
  }


  /**
   * Add the classpath to the parameter ClassLoader.
   *
   * @param classLoader Collection<File>
   * @param urls URL[]
   * @return ClassLoader
   */
  public static ClassLoader addClassPath(ClassLoader classLoader, URL[] urls) throws Exception {
    List<File> fileList = new ArrayList<>();
    for (URL url : urls) {
      File file = new File(url.toURI());
      fileList.add(file);
    }
    return addClassPath(classLoader, fileList);
  }

}
