package milktea.cim.framework.extension;

import java.util.logging.Logger;

public class Extension {

  private final Logger logger = Logger
    .getLogger("cim-root://framework/extensions/" + getClass().getSimpleName() + "/log");

  public Logger getLogger() {
    return logger;
  }

}
