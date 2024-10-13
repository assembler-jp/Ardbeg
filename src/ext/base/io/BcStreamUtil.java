package ext.base.io;

import java.io.*;
import ext.base.log.*;


/**
 * Stream utility
 */
public final class BcStreamUtil extends Object {

  /** constructor */
  private BcStreamUtil() {
    super();
  }


  /**
   * Closes obj if it implements Closeable.
   *
   * @param obj Object
   * @return boolean true if successful
   * */
  public static boolean close(Object obj) {
    boolean success = false;
    if (obj != null) {
      if (obj instanceof Closeable) {
        Closeable closeable = (Closeable)obj;
        try {
          closeable.close();
          success = true;
        } catch (Exception ex) {
          BcLogUtil.error(ex);
        }
      }
    }
    return success;
  }


}
