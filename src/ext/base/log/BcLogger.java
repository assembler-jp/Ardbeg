package ext.base.log;


/**
 * Logger interface
 */
public interface BcLogger {

  /**
   * Level=INFO
   *
   * @param log
   */
  public void info(Object log);

  /**
   * Level=ERROR
   *
   * @param log
   */
  public void error(Object log);

  /**
   * Level=WARN
   *
   * @param log
   */
  public void warning(Object log);

  /**
   * Level=DEBUG
   *
   * @param log
   */
  public void debug(Object log);

  /**
   * Level=TRACE
   *
   * @param log
   */
  public void trace(Object log);

}
