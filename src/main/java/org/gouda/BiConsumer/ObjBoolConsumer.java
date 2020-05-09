package org.gouda.BiConsumer;

@FunctionalInterface
public interface ObjBoolConsumer<T> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t the first input argument
   * @param value the second input argument
   */
  void accept(T t, boolean value);
}