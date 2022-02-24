package com.ontotext.refine.client;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.AfterClass;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

/**
 * Base class for all integration tests which execute operations against existing OntoRefine
 * instance.<br>
 * The class contains logic for spawning GraphDB with OntoRefine instance using Docker image with
 * specific version. The container will be reused by all concrete implementations, which should not
 * be coupled by any projects or resources created by other test/s in order to keep the environment
 * clean.<br>
 * Additionally there is a option to enable/disable the security of the GDB instance.
 *
 * @author Antoniy Kunchev
 */
@Testcontainers
public abstract class IntegrationTest {

  private static final String DEFAUL_GDB_DOCKER_IMAGE = "ontotext/graphdb:10.0.0-M1";

  // Tries to retrieve the image name from the surefire plugin property. Otherwise uses the default
  private static final DockerImageName GDB_DOCKER_IMAGE_NAME =
      DockerImageName.parse(System.getProperty("graphdb.docker.image", DEFAUL_GDB_DOCKER_IMAGE));

  private static final int PORT = 7200;

  protected static final String REPO_NAME = "integration-tests";

  protected static final GenericContainer<?> GDB_DOCKER;

  // starts the container manually as we want to reuse it for all concrete tests.
  // It will be stopped automatically, once all tests are executed
  static {
    GDB_DOCKER = new GenericContainer<>(GDB_DOCKER_IMAGE_NAME)
        .withExposedPorts(PORT)
        .withLogConsumer(frame -> System.out.print(frame.getUtf8String()))
        .withCopyFileToContainer(
            MountableFile.forClasspathResource("/integration/config.ttl"),
            "/opt/graphdb/home/data/repositories/" + REPO_NAME + "/config.ttl")
        .waitingFor(Wait.forLogMessage(".*Started GraphDB in workbench mode at port 7200.*", 1));

    GDB_DOCKER.start();
  }

  private RefineClient refineClient;
  private RefineClient securedRefineClient;

  private static boolean isSecurityEnabled;

  @AfterClass
  private static void afterAll() {
    isSecurityEnabled = false;
  }

  /**
   * Enables/disables the security of the GraphDB and the {@link RefineClient} instance that will be
   * used in the tests.
   *
   * @param turn controls the switching
   */
  protected void security(Turn turn) {
    isSecurityEnabled = Turn.ON.equals(turn);
    if (isSecurityEnabled && securedRefineClient == null) {
      initSecurity();
    }
  }

  /**
   * Enables or disables the security of GpraphDB instance.<br>
   * The functionality is achieved using the REST service of GDB.
   */
  private void initSecurity() {
    try {
      BasicHttpEntity entity = new BasicHttpEntity();
      entity.setContent(
          IOUtils.toInputStream(Boolean.toString(isSecurityEnabled), StandardCharsets.UTF_8));
      HttpUriRequest request =
          RequestBuilder.post(getClient().createUri("/rest/security"))
              .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
              .setEntity(entity)
              .build();

      getClient().execute(request, response -> {
        StatusLine statusLine = response.getStatusLine();
        if (HttpStatus.SC_OK != statusLine.getStatusCode()) {
          fail("Failed to enable the security of the GraphDB instance due to: " + statusLine);
        }
        return response;
      });
    } catch (IOException ioe) {
      fail("Failed to enable the security of the GraphDB instance.");
    }
  }

  /**
   * Provides instance of {@link RefineClient}.
   *
   * @return refine client
   */
  protected RefineClient getClient() {
    return isSecurityEnabled ? getSecuredRefineClient() : getRefineClient();
  }

  private RefineClient getSecuredRefineClient() {
    if (securedRefineClient == null) {
      try {
        HttpHost httpHost = new HttpHost(GDB_DOCKER.getHost(), GDB_DOCKER.getMappedPort(PORT));
        BasicCredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "root"));
        return RefineClients.securityAware(httpHost.toURI(), provider);
      } catch (URISyntaxException uriExc) {
        throw new RuntimeException("Failed to create secured refine client.", uriExc);
      }
    }
    return securedRefineClient;
  }

  private RefineClient getRefineClient() {
    if (refineClient == null) {
      try {
        HttpHost httpHost = new HttpHost(GDB_DOCKER.getHost(), GDB_DOCKER.getMappedPort(PORT));
        return RefineClients.standard(httpHost.toURI());
      } catch (URISyntaxException uriExc) {
        throw new RuntimeException("Failed to create refine client.", uriExc);
      }
    }
    return refineClient;
  }

  /**
   * Contains switch options.
   *
   * @author Antoniy Kunchev
   */
  public enum Turn {
    ON, OFF
  }
}
