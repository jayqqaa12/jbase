package com.jayqqaa12.jbase.cache.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author zhengqi.guo, zhengqi.guo@leyantech.com.
 * @date 2019-06-13.
 */
public final class LambdaExceptionUtil {


  @FunctionalInterface
  public interface ConsumerWithThrowable<T, E extends Throwable> {

    void accept(T t) throws E;
  }

  @FunctionalInterface
  public interface BiConsumerWithThrowable<T, U, E extends Throwable> {

    void accept(T t, U u) throws E;
  }

  @FunctionalInterface
  public interface FunctionWithThrowable<T, R, E extends Throwable> {

    R apply(T t) throws E;
  }

  @FunctionalInterface
  public interface SupplierWithThrowable<T, E extends Throwable> {

    T get() throws E;
  }

  @FunctionalInterface
  public interface RunnableWithThrowable<E extends Throwable> {

    void run() throws E;
  }

  /**
   * .forEach(rethrowConsumer(name -> System.out.println(Class.forName(name)))); or
   * .forEach(rethrowConsumer(ClassNameUtil::println));
   */
  public static <T, E extends Throwable> Consumer<T> rethrowConsumer(
      ConsumerWithThrowable<T, E> consumer) throws E {
    return t -> {
      try {
        consumer.accept(t);
      } catch (Throwable Throwable) {
        throwAsUnchecked(Throwable);
      }
    };
  }

  public static <T, U, E extends Throwable> BiConsumer<T, U> rethrowBiConsumer(
      BiConsumerWithThrowable<T, U, E> biConsumer) throws E {
    return (t, u) -> {
      try {
        biConsumer.accept(t, u);
      } catch (Throwable Throwable) {
        throwAsUnchecked(Throwable);
      }
    };
  }

  /**
   * .map(rethrowFunction(name -> Class.forName(name))) or .map(rethrowFunction(Class::forName))
   */
  public static <T, R, E extends Throwable> Function<T, R> rethrowFunction(
      FunctionWithThrowable<T, R, E> function) throws E {
    return t -> {
      try {
        return function.apply(t);
      } catch (Throwable Throwable) {
        throwAsUnchecked(Throwable);
        return null;
      }
    };
  }

  /**
   * rethrowSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),
   */
  public static <T, E extends Throwable> Supplier<T> rethrowSupplier(
      SupplierWithThrowable<T, E> function) throws E {
    return () -> {
      try {
        return function.get();
      } catch (Throwable Throwable) {
        throwAsUnchecked(Throwable);
        return null;
      }
    };
  }

  /**
   * uncheck(() -> Class.forName("xxx"));
   */
  public static void uncheck(RunnableWithThrowable t) {
    try {
      t.run();
    } catch (Throwable Throwable) {
      throwAsUnchecked(Throwable);
    }
  }

  /**
   * uncheck(() -> Class.forName("xxx"));
   */
  public static <R, E extends Throwable> R uncheck(SupplierWithThrowable<R, E> supplier) {
    try {
      return supplier.get();
    } catch (Throwable Throwable) {
      throwAsUnchecked(Throwable);
      return null;
    }
  }

  /**
   * uncheck(Class::forName, "xxx");
   */
  public static <T, R, E extends Throwable> R uncheck(FunctionWithThrowable<T, R, E> function,
      T t) {
    try {
      return function.apply(t);
    } catch (Throwable Throwable) {
      throwAsUnchecked(Throwable);
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private static <E extends Throwable> void throwAsUnchecked(Throwable Throwable) throws E {
    throw (E) Throwable;
  }

}
