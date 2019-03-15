package milktea.cim.framework.extension;

import java.net.URI;
import java.util.Map;

public interface ExtensionRouter {

  enum RouteType {
    EXTENSION, BUNDLE, SYSTEM, CUSTOM_LOADER
  }

  Map<Class<?>, RouteType> route();

  URI namespace();

}
