package ext.base.log;

import java.io.*;
import java.util.*;
import java.util.Date;
import java.util.logging.*;
import ext.base.runtime.*;


/**
 * BcLogger implementation
 */
public class BcLoggerImpl implements BcLogger {

  private Logger jLogger;

  /** constructor */
  public BcLoggerImpl() throws Exception {
    super();
    this.setup();
  }


  /** setup */
  protected void setup() throws Exception {
    jLogger = Logger.getLogger(""); // root
    //System.out.println("jLogger.getParent()="+jLogger.getParent());

    Handler[] handlers = jLogger.getHandlers();
    //System.out.println("handlers.length="+handlers.length);

    Level level = Level.FINE;
    ConsoleHandler consoleHandler = null;
    for (Handler handler : handlers) {
      //System.out.println("handler=" + handler.getClass().getName());
      if (handler instanceof ConsoleHandler) {
        consoleHandler = (ConsoleHandler)handler;
        break;
      }
    }
    if (consoleHandler == null) {
      consoleHandler = new ConsoleHandler();
      jLogger.addHandler(consoleHandler);
    }

    java.util.logging.Formatter formatter = new BcLogFormatter();

    consoleHandler.setFormatter(formatter);
    consoleHandler.setLevel(level);

    if (false) {
    File userDir = BcRuntimeUtil.getUserDir();
    File logFile = new File(userDir, "default.log");
    FileHandler fileHandler = new FileHandler(logFile.getPath(), true);
    fileHandler.setFormatter(formatter);
    jLogger.addHandler(fileHandler);
    }

    jLogger.setLevel(level);
   }


  @Override
  public void info(Object log) {
    jLogger.info(BcLogUtil.convertMessage(log));
  }

  @Override
  public void error(Object log) {
    jLogger.severe(BcLogUtil.convertMessage(log));
  }

  @Override
  public void warning(Object log) {
    jLogger.warning(BcLogUtil.convertMessage(log));
  }

  @Override
  public void debug(Object log) {
    jLogger.config(BcLogUtil.convertMessage(log));
  }

  @Override
  public void trace(Object log) {
    jLogger.fine(BcLogUtil.convertMessage(log));
  }

  public Logger getLogger() {
    return jLogger;
  }

  public void setLogger(Logger jLogger) {
    this.jLogger = jLogger;
  }


  /** BcLogFormatter */
  class BcLogFormatter extends SimpleFormatter {

    private String format;
    private int levelNameLength;
    private Map<Integer, String> levelNameMap = new HashMap<>();
 
    /** constructor */
    public BcLogFormatter() {
      super();

      levelNameMap.put(Level.OFF.intValue(), "OFF");
      levelNameMap.put(Level.SEVERE.intValue(), "SEVERE");
      levelNameMap.put(Level.WARNING.intValue(), "WARNING");
      levelNameMap.put(Level.INFO.intValue(), "INFO");
      levelNameMap.put(Level.CONFIG.intValue(), "CONFIG");
      levelNameMap.put(Level.FINE.intValue(), "FINE");
      levelNameMap.put(Level.FINER.intValue(), "FINER");
      levelNameMap.put(Level.FINEST.intValue(), "FINEST");
      levelNameMap.put(Level.ALL.intValue(), "ALL");

      for (Map.Entry<Integer, String> mapEntry : levelNameMap.entrySet()) {
        levelNameLength = Math.max(levelNameLength, mapEntry.getValue().length());
      }
      format = "[%1$tF %1$tT.%1$tL] [%2$-"+ levelNameLength +"s] %3$s %n";
    }


    @Override
    public synchronized String format(LogRecord lr) {
      String levelName = levelNameMap.get(lr.getLevel().intValue());
      return String.format(format, new Date(lr.getMillis()), levelName, lr.getMessage());
     }
  };

}
