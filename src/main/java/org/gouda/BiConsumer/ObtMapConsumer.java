package org.gouda.BiConsumer;

import java.util.Map;

@FunctionalInterface
public interface ObtMapConsumer<T, M extends Map<? extends Comparable, ?>> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t     the first input argument
   * @param value the second input argument
   */
  void accept(T t, M value);

}