package org.gouda.BiConsumer;

@FunctionalInterface
public interface ObjStringConsumer<T> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t the first input argument
   * @param value the second input argument
   */
  void accept(T t, String value);
}