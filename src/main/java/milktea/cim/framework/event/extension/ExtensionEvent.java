package milktea.cim.framework.event.extension;

import milktea.cim.framework.extension.Extension;

public class ExtensionEvent {

  protected final Extension extension;

  public ExtensionEvent(Extension extension) {
    this.extension = extension;
  }

  public Extension getExtension() {
    return extension;
  }

}
