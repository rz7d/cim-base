package milktea.cim.framework.event.extension;

public interface CancellableEvent {

  boolean isCancelled();

  boolean cancel();

}
