package org.gouda.BiConsumer;

import java.util.Collection;

@FunctionalInterface
public interface ObjCollectionConsumer<T , C extends Collection<?>> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t the first input argument
   * @param value the second input argument
   */
  void accept(T t, C value);

}