package milktea.cim.framework.command;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

public class CommandDescriptorTest {

  @Test
  public void testHashCode() {
    final Consumer<Object> c = __ -> {
    };
    var a = new CommandDescriptor("", "", "", getClass(), c);
    var b = new CommandDescriptor("", "", "", getClass(), c);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void testEquals() {
    final Consumer<Object> c = __ -> {
    };
    var a = new CommandDescriptor("", "", "", getClass(), c);
    var b = new CommandDescriptor("", "", "", getClass(), c);
    assertEquals(a.equals(b), b.equals(a));
  }

  @Test
  public void testMethodValidationThrown() throws Exception {
    var method = getClass().getDeclaredMethod("testMethodValidationThrown");
    assertThrows(IllegalArgumentException.class,
      () -> CommandDescriptor.ofMethod(this, method));
  }

  @Test
  public void testMethodValidationSuccess() throws Exception {
    var method = getClass().getDeclaredMethod("dummy", Object.class);
    assertDoesNotThrow(() -> CommandDescriptor.ofMethod(this, method));
  }

  @Command(name = "", description = "", permission = "")
  public void dummy(Object n) {

  }

}
