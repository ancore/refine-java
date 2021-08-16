package com.ontotext.refine.client.util;


/**
 * A {@link FunctionalInterface} that can be used to return an object and potentially throw a
 * {@link Throwable}.
 *
 * @author Antoniy Kunchev
 *
 * @param <D> the type of the data
 * @param <T> the type of the {@link Throwable}
 */
@FunctionalInterface
public interface ThrowingSupplier<D, T extends Throwable> {

  /**
   * Retrieves the result.
   *
   * @return a result
   * @throws T when error occurs
   */
  D get() throws T;
}
