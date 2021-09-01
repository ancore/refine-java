package com.ontotext.refine.client.command.preferences;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;

import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;


/**
 * A command to retrieve the preference for specific property.
 *
 * @author Antoniy Kunchev
 */
public class GetPreferenceCommand implements RefineCommand<GetPreferenceCommandResponse> {

  private final String property;

  private GetPreferenceCommand(String property) {
    this.property = property;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/get-preference";
  }

  @Override
  public GetPreferenceCommandResponse execute(RefineClient client) throws RefineException {
    try {
      HttpUriRequest request = RequestBuilder
          .get(client.createUrl(endpoint()).toString())
          .addParameter("name", property)
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException("Failed to retrieve preferences due to: %s", ioe.getMessage());
    }
  }

  @Override
  public GetPreferenceCommandResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);
    JsonNode json = JSON_PARSER.parseJson(response.getEntity().getContent());
    return new GetPreferenceCommandResponse(json);
  }

  /**
   * Builder for {@link GetPreferenceCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class Builder {

    private String property;

    public Builder setProperty(String property) {
      this.property = property;
      return this;
    }

    /**
     * Builds the {@link GetPreferenceCommand}.
     *
     * @return a command
     */
    public GetPreferenceCommand build() {
      Validate.notBlank(property, "Missing 'property' argument");
      return new GetPreferenceCommand(property);
    }
  }
}
