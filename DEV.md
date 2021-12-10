# OntoRefine-client development guide

## Prerequisites

- Java 8
- Maven
- (*Optional*) Google's Java Style Checkstyle included in the IDE

## Development

The library contains various abstractions and classes which provide convenience in order to make the development of 
new command as easy as possible.

Following the main pattern, to introduce an new command in the library, the developer have to implement few simple
classes.

There are two main classes needed for every command - the main class with the command logic and the class which will
represent the response from the execution.

The main class which will hold the logic of the command, in most cases should implement `RefineCommand<T>` interface,
where the `<T>` is the type of the response. This interface contains two method definitions on it own, plus one more
as it extends `ResponseHandler<T>`. Implementing the methods will provide a new command which can be registered in
`RefineCommands`, which allows easy access to the functionalities that the library provides to its users.


### Command methods implementation

##### Endpoint

As each command represents execution of HTTP request to the REST API of the OntoRefine tool, the first thing required
for every implementation is the actual endpoint for the request. It is provided by implementing the `String endpoint()`
method. The implementation of the method should provide an URL path without the base and beginning with slash `\`. For
example, if the full URL is `http://graphdb:7200/orefine/command/core/export-rows`, the endpoint method of the
command should return only `/orefine/command/core/export-rows`. The base of the URL is added automatically, when
the request object is created.

##### Execution

The logic of the command should be implemented in `T execute(RefineClient client)` method. What it does is basically
create the request by using the endpoint and any additional parameters required for the command execution and than
actually execute it, using the client.

##### Response

As mentioned, the `RefineCommand<T>` interface extends `ResponseHandler<T>`, which allows the command itself to
become the response handler for the request. The implementation of the `T handleResponse(HttpResponse response)`
method does exactly that. The usual implementation of the method includes check for the response status and creation
of response object using the content of the response, if there is any. The response object can be plain DTO or it can
extend `RefineResponse`, which provides an status of the response and message providing details in case of an error.

#### Command instantiation

It is encouraged, but not required for the command instantiation to be used builder. Using a builder allows better
encapsulation and validation of the required parameters for the command, easy extensibility, without introducing
compile errors in dependent projects and so on.

When the command is registered in `RefineCommands` interface, it actually provides the builder for the command so
all of the parameters can be passed and than the command itself can be created.

#### Example

```java
import static org.apache.http.client.methods.RequestBuilder.get;
import static org.apache.http.util.Asserts.notEmpty;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.RefineResponse;
import com.ontotext.refine.client.ResponseCode;
import com.ontotext.refine.client.command.ExampleCommand.ExampleResponse;
import com.ontotext.refine.client.exceptions.RefineException;
import com.ontotext.refine.client.util.HttpParser;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;

/** A command used for example */
public class ExampleCommand implements RefineCommand<ExampleResponse> {

  private final String paramerter;

  private ExampleCommand(String paramerter) {
    this.paramerter = paramerter;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/example";
  }

  @Override
  public ExampleResponse execute(RefineClient client) throws RefineException {
    try {
      String uri = client.createUrl(endpoint()).toString();
      HttpUriRequest request = get(uri).addParameter("param", paramerter).build();
      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException("Failed to execute the example command due to: %s", ioe.getMessage());
    }
  }

  @Override
  public ExampleResponse handleResponse(HttpResponse response) throws IOException {
    try {
      HttpParser.HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);
      return ExampleResponse.ok(response.getEntity().getContent());
    } catch (Exception exc) {
      return ExampleResponse.error(exc.getMessage());
    }
  }

  // example of response object
  public static class ExampleResponse extends RefineResponse {

    private final InputStream content;

    private ExampleResponse(ResponseCode code, String message, InputStream content) {
      super(code, message);
      this.content = content;
    }

    static ExampleResponse ok(InputStream content) {
      return new ExampleResponse(ResponseCode.OK, null, content);
    }

    static ExampleResponse error(String message) {
      notEmpty(message, "The message should not be empty.");
      return new ExampleResponse(ResponseCode.ERROR, message, null);
    }

    public InputStream getContent() {
      return content;
    }
  }

  // the builder of the command with single parameter
  public static class Builder {

    private String param;

    public Builder setParameter(String param) {
      this.param = param;
      return this;
    }

    public ExampleCommand build() {
      Validate.notBlank(param, "The argument 'parameter' is blank");
      return new ExampleCommand(param);
    }
  }
}

```


## Testing

Every command includes a proper set of test cases, which are verifying the behavior of the command and all of the
validations that it has. As a minimum the command should have simple Unit tests covering the basic scenario,
where everything is provided correctly and the command execution is completed successfully. This will guarantee that
any future changes to the command will not break the main functionality. Furthermore when the main scenario of the
command execution is provided it gives insight on what the command actually does, if the code is too complex to be
understood initially.

There is a class called `BaseCommandTest`, which provides some convenient methods and mocks for easier development
of the tests for the commands.

### Integration testing

As of version `1.2.0`, the library contains infrastructure for integration tests. They are taking advantage of the
test library [Testcontainers](https://www.testcontainers.org/), which allows management of `Docker` containers in
`JUnit` tests. `Testcontainers` is used to spawn a `GraphDB` docker image with specific version, which provides
access to actual `OntoRefine` tool instance, where the integration tests can execute different commands and verify
the results.

At the moment there are few integration tests, which are using and testing the commands that are currently available
in the library by executing specific scenarios. The scenarios are using the commands to complete a specific job.
They are validating straightforward execution and expect the commands to be completed successfully.

The management of the `GraphDB` container is done in `IntegrationTest`, which basically creates singleton instance 
for the container, which will be reused in all concrete test implementations. This is done in order to save time and
resources, when executing multiple tests. However sharing single instance between multiple tests requires careful
management of the resources between the tests. In order to avoid errors and coupling between the tests by reusing for
example projects or something else, it is encouraged every test to manage their own scope and resources, even when it 
is more convenient to reuse some resource from another test.

There is another specific class for the commands integration tests called `CommandIntegrationTest`, which
provides convenient method for execution of commands for retrieving CSRF token, creation of project, etc.

Although most of the commands and their functionality is covered by the integration tests, unit tests are still going
to be written. They should cover and verify the error handling, commands building validation, parameters syntax, etc.
cases, which are not optimal for integration testing.

There is a possibility in the future to extend the integration tests to run with different version of `GraphDB`
containers in order to verify compatibility with different versions, but it will require some additional work to be
done in order to optimize the execution and the container management.


## Releases

The releases of the library are done regularly, when new functionality or bug fixes without workaround are pushed and
verified.

Exceptional case where new release might be published is in case of vulnerability issue detected by the pipeline or
other scanning tool.

The repository contains a release pipeline, which uses GitHub actions for execution. The pipeline configuration is
currently placed in `.github/package-publish.yml`. It is triggered, when it detects that a new release is published
via the `Releases` page in the repository.

To release the library you need to have correct access and permissions. If you do, simply go on the
[New Release Page](https://github.com/Ontotext-AD/ontorefine-client/releases/new), set the correct version and tag that
needs to be released, input the changes that are included in the version and press the release button.

When the pipeline completes the packages will be published in the Ontotext Public Nexus Repository, where they will be
available for download and usage.
