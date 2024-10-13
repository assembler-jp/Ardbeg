package ext.base.log;

import java.io.*;
import ext.base.io.*;


/**
 * Logging utility
 */
public final class BcLogUtil extends Object {

	private static BcLogger loggerDefault;
	static {
		try {
			loggerDefault = new BcLoggerImpl();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

  /** constructor */
  private BcLogUtil() {
    super();
  }


  /**
   * 引数 throwable のスタックトレースを文字列にします。
   *
   * @param throwable Throwable
   * @return String
   */
  public static String getStackTrace(Throwable throwable) {
    StringWriter sw = null;
    PrintWriter pw = null;
    String stackTrace = null;
    try {
      sw = new StringWriter();
      pw = new PrintWriter(sw);
      throwable.printStackTrace(pw);
      pw.flush();
      stackTrace = sw.getBuffer().toString();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      BcStreamUtil.close(sw);
      BcStreamUtil.close(pw);
    }
    return stackTrace;
  }


  /**
   * Throwable を考慮したメッセージの変換を行います。
   *
   * @param message
   * @return 変換されたメッセージ
   */
  public static String convertMessage(Object message) {
    if (message == null) {
      return null;
    }
    String newMessage = null;
    if (Throwable.class.isAssignableFrom(message.getClass())) {
      Throwable throwable = (Throwable)message;
      newMessage = getStackTrace(throwable);
    } else {
      newMessage = message.toString();
    }
    return newMessage;
  }


  /**
   * デフォルトの BcLogger を返します。
   *
   * @return BcLogger
   */
  public static BcLogger getDefaultLogger() {
    return loggerDefault;
  }


  /**
   * info
   *
   * @param log ログオブジェクト
   */
  public static void info(Object log) {
    loggerDefault.info(log);
  }


  /**
   * error
   *
   * @param log ログオブジェクト
   */
  public static void error(Object log) {
    loggerDefault.error(log);
  }


  /**
   * warning
   *
   * @param log ログオブジェクト
   */
  public static void warning(Object log) {
    loggerDefault.warning(log);
  }


  /**
   * debug
   *
   * @param log ログオブジェクト
   */
  public static void debug(Object log) {
    loggerDefault.debug(log);
  }

}
