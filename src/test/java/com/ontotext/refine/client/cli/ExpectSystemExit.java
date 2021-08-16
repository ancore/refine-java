package com.ontotext.refine.client.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.security.Permission;
import java.util.Optional;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;


/**
 * Provides a way to assert the {@link System#exit(int)} code on tests which are verifying certain
 * system behavior.<br>
 * Keep in mind that such test will interrupt their execution once the actual logic for
 * {@link System#exit(int)} is invoked and any subsequent asserts will be skipped. If additional
 * asserts are required, they should be placed in try-finally block in order to ensure that they
 * will be executed.<br>
 * Example of usage:
 *
 * <pre>
 * <code>
 * &#64;Test
 * &#64;ExpectSystemExit(1)
 * void testMethod() {
 *   try {
 *     // code which invokes System.exit
 *   } finally {
 *     // additional assertions
 *   }
 * }
 * </code>
 * </pre>
 *
 * @author Antoniy Kunchev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ExtendWith(ExpectSystemExit.SystemExitVerifier.class)
public @interface ExpectSystemExit {

  /**
   * The expected code with which {@link System#exit(int)} should be invoked.
   *
   * @return exit code
   */
  int value() default 0;

  /**
   * {@link ExpectSystemExit} annotation processor which handles the tests that should check the
   * {@link System#exit(int)} conditions.
   *
   * @author Antoniy Kunchev
   */
  static class SystemExitVerifier
      implements AfterEachCallback, BeforeEachCallback, TestExecutionExceptionHandler {

    private int expected;
    private SecurityManagerProxy proxy = new SecurityManagerProxy(System.getSecurityManager());
    private SecurityManager systemSecurityManager;

    @Override
    public void beforeEach(final ExtensionContext context) {
      systemSecurityManager = System.getSecurityManager();
      getAnnotation(context).ifPresent(code -> expected = code.value());
      System.setSecurityManager(proxy);
    }

    private Optional<ExpectSystemExit> getAnnotation(ExtensionContext context) {
      return findAnnotation(context.getTestMethod(), ExpectSystemExit.class)
          .or(() -> findAnnotation(context.getTestClass(), ExpectSystemExit.class));
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
        throws Throwable {
      if (throwable instanceof ExitException) {
        // we expect this exception when the system exits via System.exit invocation
        return;
      }
      throw throwable;
    }

    @Override
    public void afterEach(final ExtensionContext context) {
      System.setSecurityManager(systemSecurityManager);
      assertEquals(
          expected, proxy.code, "Expected System.exit(" + expected + ") but it was never invoked.");
    }
  }

  /**
   * Custom {@link SecurityManager} that acts as a proxy for the system {@link SecurityManager}. It
   * handles the exit check by throwing custom exception, which is caught and handled by the
   * {@link SystemExitVerifier}.
   */
  static class SecurityManagerProxy extends SecurityManager {

    private int code;
    private final Optional<SecurityManager> delegateeOpt;

    private SecurityManagerProxy(SecurityManager delegatee) {
      this.delegateeOpt = Optional.ofNullable(delegatee);
    }

    @Override
    public void checkExit(int exitCode) {
      this.code = exitCode;
      throw new ExitException(exitCode);
    }

    @Override
    public void checkPermission(Permission perm) {
      delegateeOpt.ifPresent(sm -> sm.checkPermission(perm));
    }
  }

  /**
   * Thrown by the custom test {@link SecurityManager} when checking the exit status code.
   *
   * @author Antoniy Kunchev
   */
  static class ExitException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final int statusCode;

    private ExitException(int statusCode) {
      this.statusCode = statusCode;
    }

    public int getStatusCode() {
      return statusCode;
    }
  }
}
