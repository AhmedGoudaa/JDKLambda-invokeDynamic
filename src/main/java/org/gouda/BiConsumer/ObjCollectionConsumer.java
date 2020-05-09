package org.gouda.BiConsumer;

import java.util.Collection;

@FunctionalInterface
public interface ObjCollectionConsumer<T> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t the first input argument
   * @param value the second input argument
   */
  void accept(T t, Collection value);

}