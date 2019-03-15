package milktea.cim.framework.connector;

public interface ServiceConnector {

  boolean isConnected();

  /**
   *
   * login() method is not standard method. Use connect() method instead.
   *
   * @return
   */
  Exception connect();

  default void connectExceptionally() throws Exception {
    final Exception exception = connect();
    if (exception != null)
      throw exception;
  }

}
