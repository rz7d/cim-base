package milktea.cim.framework.event;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventBus {

  public static void main(String[] args) {
    class Listener {
      @EventHandler
      public void onMessage(String message) {
        System.out.println(message);
      }
    }

    var bus = new EventBus();
    bus.register(new Listener());
    bus.register(new Object() {
      @EventHandler
      public void onBigInteger(BigInteger bigInteger) {
        System.out.println(bigInteger.nextProbablePrime());
      }
    });

    var proxy = (EventHandler) Proxy.newProxyInstance(
      EventBus.class.getClassLoader(),
      new Class<?>[] { EventHandler.class },
      (p, method, arguments) -> {
        return null;
      });
    System.out.println(proxy);

    bus.fire("hello :)");

    var random = new Random();
    bus.fire(new BigInteger(Math.abs(random.nextLong()) + "" + Math.abs(random.nextLong())));
    // ObjectのhashCodeはJava SE 8以降、XORShiftによる乱数なので、簡単な乱数として使えます。
  }

  static class HandlerSet {

    private final Class<?> eventType;
    private final Consumer<? super Object> handler;

    public HandlerSet(Class<?> eventType, Consumer<? super Object> handler) {
      this.eventType = eventType;
      this.handler = handler;
    }

    public Class<?> getEventType() {
      return eventType;
    }

    public Consumer<? super Object> getHandler() {
      return handler;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj != null && obj instanceof HandlerSet) {
        HandlerSet o = (HandlerSet) obj;
        return (Objects.equals(getEventType(), o.getEventType()) && Objects.equals(getHandler(), o.getHandler()));
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(eventType, handler);
    }

    public static Collection<? extends HandlerSet> from(Object handlers) {
      final Class<?> type = handlers.getClass();
      return Stream.of(type.getMethods(), type.getDeclaredMethods())
        .flatMap(Arrays::stream)
        .filter(m -> m.getAnnotation(EventHandler.class) != null)
        .filter(m -> m.getParameterCount() == 1)
        .distinct()
        .map(m -> new HandlerSet(m.getParameterTypes()[0], newConsumer(handlers, m)))
        .collect(Collectors.toSet());
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

  private final Collection<HandlerSet> handlers = Collections.synchronizedCollection(new ArrayList<>());

  public boolean register(Object listener) {
    return handlers.addAll(HandlerSet.from(listener));
  }

  public boolean unregister(Object listener) {
    return handlers.removeAll(HandlerSet.from(listener));
  }

  public void fire(Object event) {
    handlers.stream()
      .filter(h -> h.getEventType().isInstance(event))
      .map(HandlerSet::getHandler)
      .forEach(h -> h.accept(event));
  }

}
