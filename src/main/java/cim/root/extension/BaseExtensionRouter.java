package cim.root.extension;

import java.net.URI;
import java.util.Map;

import milktea.cim.boot.Base;
import milktea.cim.framework.extension.ExtensionRouter;

public class BaseExtensionRouter implements ExtensionRouter {

  @Override
  public Map<Class<?>, RouteType> route() {
    return Map.of(Base.class, RouteType.SYSTEM);
  }

  @Override
  public URI namespace() {
    return URI.create("cim-extension:///base");
  }

}
