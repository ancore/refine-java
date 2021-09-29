package com.ontotext.refine.client;

import java.net.URISyntaxException;
import org.apache.http.HttpHost;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Base class for all integration tests which are going to execute operations against existing
 * OntoRefine instance.<br>
 * The class contains logic for spawning GraphDB with OntoRefine instance using Docker image with
 * specific version. The container will be reused by all concrete implementations, which should not
 * be coupled by any projects or resources created by other test/s in order to keep the environment
 * clean.
 *
 * @author Antoniy Kunchev
 */
@Testcontainers
public abstract class IntegrationTest {

  private static final String DEFAUL_GDB_DOCKER_IMAGE = "ontotext/graphdb:9.10.0-ee";

  // Tries to retrieve the image name from the surefire plugin property. Otherwise uses the default
  private static final DockerImageName GDB_DOCKER_IMAGE_NAME =
      DockerImageName.parse(System.getProperty("graphdb.docker.image", DEFAUL_GDB_DOCKER_IMAGE));

  private static final int PORT = 7200;

  protected static final GenericContainer<?> GDB_DOCKER;

  // starts the container manually as we want to reuse it for all concrete tests.
  // It will be stopped automatically, once all tests are executed
  static {
    GDB_DOCKER = new GenericContainer<>(GDB_DOCKER_IMAGE_NAME)
        .withExposedPorts(PORT)
        .withLogConsumer(frame -> System.out.print(frame.getUtf8String()))
        .waitingFor(Wait.forLogMessage(".*Started GraphDB in workbench mode at port 7200.*", 1));

    GDB_DOCKER.start();
  }

  private RefineClient refineClient;

  /**
   * Provides instance of {@link RefineClient}.
   *
   * @return refine client
   */
  protected RefineClient getClient() {
    if (refineClient == null) {
      try {
        HttpHost httpHost = new HttpHost(GDB_DOCKER.getHost(), GDB_DOCKER.getMappedPort(PORT));
        refineClient = RefineClients.create(httpHost.toURI());
      } catch (URISyntaxException uriExc) {
        throw new RuntimeException("Failed to create refine client.", uriExc);
      }
    }

    return refineClient;
  }
}
