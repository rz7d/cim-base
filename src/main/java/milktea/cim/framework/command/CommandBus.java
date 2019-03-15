package milktea.cim.framework.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * CommandBus クラスは、コマンド バスとしての能力を持つミュータブル オブジェクトを表します。
 *
 *
 * @author azure
 *
 */
public final class CommandBus {

  private final Collection<CommandDescriptor> commands = Collections.synchronizedCollection(new ArrayList<>());

  public CommandBus() {
  }

  /**
   * 指定されたコマンド オブジェクトを、このコマンド バスに登録します。
   *
   * @param command 登録するコマンド オブジェクト。
   * @return 登録に成功したかどうか。
   */
  public boolean register(Object command) {
    return commands.addAll(CommandDescriptor.ofAll(command));
  }

  /**
   * 指定されたコマンド オブジェクトを、このコマンド バスから削除します。
   *
   * @param command 削除するコマンド オブジェクト。
   * @return 削除に成功したかどうか。
   */
  public boolean unregister(Object listener) {
    return commands.removeAll(CommandDescriptor.ofAll(listener));
  }

  /**
   * コマンドを実行します。
   *
   * @param command          実行するコマンドの名前。
   * @param commandArguments コマンドの引数。
   */
  public void execute(String command, Object commandArguments) {
    Objects.requireNonNull(command);
    Objects.requireNonNull(commandArguments);

    var trimmedCommand = command.trim();
    commands.stream()
      .filter(c -> c.getCommand().equalsIgnoreCase(trimmedCommand))
      .filter(c -> c.getEventType().isInstance(commandArguments))
      .peek(c -> c.getDescription())
      .map(CommandDescriptor::getExecutor)
      .forEach(h -> h.accept(commandArguments));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((commands == null) ? 0 : commands.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CommandBus other = (CommandBus) obj;
    if (commands == null) {
      if (other.commands != null)
        return false;
    } else if (!commands.equals(other.commands))
      return false;
    return true;
  }

}
