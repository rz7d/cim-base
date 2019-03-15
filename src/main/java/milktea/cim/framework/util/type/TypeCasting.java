package milktea.cim.framework.util.type;

public final class TypeCasting {

  private TypeCasting() {
  }

  public static <T> T cast(Object object, Class<? extends T> type) {
    try {
      if (type.isInstance(object)) {
        return type.cast(object);
      }
      return null;
    } catch (ClassCastException exception) {
      return null;
    }
  }

}
