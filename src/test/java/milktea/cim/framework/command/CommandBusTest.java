package milktea.cim.framework.command;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CommandBusTest {

  @Test
  public void testAll() {
    class TestCommand {
      @Command(name = "test", description = "", permission = "")
      public void execute(String arg) {
        assertEquals("testmes", arg);
      }
    }
    var bus = new CommandBus();
    bus.register(new TestCommand());
    bus.execute("test", "testmes");
  }

}
