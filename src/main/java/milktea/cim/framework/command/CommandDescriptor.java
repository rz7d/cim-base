package milktea.cim.framework.command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class CommandDescriptor {

  private final String command;
  private final String description;
  private final String permission;

  private final Class<?> eventType;
  private final Consumer<? super Object> executor;

  CommandDescriptor(String command, String description, String permission, Class<?> eventType,
    Consumer<? super Object> executor) {
    this.command = Objects.requireNonNull(command);
    this.description = Objects.requireNonNull(description);
    this.permission = Objects.requireNonNull(permission);
    this.eventType = Objects.requireNonNull(eventType);
    this.executor = Objects.requireNonNull(executor);
  }

  public String getCommand() {
    return command;
  }

  public String getDescription() {
    return description;
  }

  public String getPermission() {
    return permission;
  }

  public Class<?> getEventType() {
    return eventType;
  }

  public Consumer<? super Object> getExecutor() {
    return executor;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((command == null) ? 0 : command.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
    result = prime * result + ((executor == null) ? 0 : executor.hashCode());
    result = prime * result + ((permission == null) ? 0 : permission.hashCode());
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
    CommandDescriptor other = (CommandDescriptor) obj;
    if (command == null) {
      if (other.command != null)
        return false;
    } else if (!command.equals(other.command))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (eventType == null) {
      if (other.eventType != null)
        return false;
    } else if (!eventType.equals(other.eventType))
      return false;
    if (executor == null) {
      if (other.executor != null)
        return false;
    } else if (!executor.equals(other.executor))
      return false;
    if (permission == null) {
      if (other.permission != null)
        return false;
    } else if (!permission.equals(other.permission))
      return false;
    return true;
  }

  public static Collection<? extends CommandDescriptor> ofAll(Object handlers) {
    var type = handlers.getClass();
    return Stream.of(type.getMethods(), type.getDeclaredMethods())
      .flatMap(Arrays::stream)
      .filter(m -> m.getAnnotation(Command.class) != null)
      .filter(m -> m.getParameterCount() >= 1)
      .distinct()
      .map(m -> ofMethod(handlers, m))
      .collect(Collectors.toUnmodifiableList());
  }

  public static CommandDescriptor ofMethod(Object o, Method method) {
    if (method.getParameterCount() < 1)
      throw new IllegalArgumentException("method.getParameterCount() must be n>0");
    var meta = method.getAnnotation(Command.class);
    if (meta == null)
      throw new IllegalArgumentException("Missing annotation " + Command.class.getName());
    var command = meta.name();
    var description = meta.description();
    var permission = meta.permission();
    var eventType = method.getParameterTypes()[0];
    var executor = newConsumer(o, method);
    return new CommandDescriptor(command, description, permission, eventType, executor);
  }

  private static Consumer<? super Object> newConsumer(Object thisObj, Method method) {
    return event -> {
      try {
        method.invoke(thisObj, event);
      } catch (RuntimeException exception) {
        throw exception;
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      }
    };
  }

}