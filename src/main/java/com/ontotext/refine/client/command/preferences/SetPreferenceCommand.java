package com.ontotext.refine.client.command.preferences;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;

import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;


/**
 * A command to set preference for specific property.
 *
 * @author Antoniy Kunchev
 */
public class SetPreferenceCommand implements RefineCommand<SetPreferenceCommandResponse> {

  private final String property;
  private final Object value;
  private final String token;

  private SetPreferenceCommand(String property, Object value, String token) {
    this.property = property;
    this.value = value;
    this.token = token;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/set-preference";
  }

  @Override
  public SetPreferenceCommandResponse execute(RefineClient client) throws RefineException {
    try {
      List<NameValuePair> form = new ArrayList<>(2);
      form.add(new BasicNameValuePair(Constants.CSRF_TOKEN, token));
      form.add(new BasicNameValuePair("value", value.toString()));

      HttpUriRequest request = RequestBuilder
          .post(client.createUrl(endpoint()).toString())
          .addParameter("name", property)
          .setEntity(new UrlEncodedFormEntity(form, StandardCharsets.UTF_8))
          .build();

      return client.execute(request, this);
    } catch (RefineException re) {
      throw re;
    } catch (IOException ioe) {
      throw new RefineException(
          "Failed to set preference for property: '%s' due to: %s",
          property,
          ioe.getMessage());
    }
  }

  @Override
  public SetPreferenceCommandResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);
    JsonNode node = JSON_PARSER.parseJson(response.getEntity().getContent());
    String code = JSON_PARSER.findExistingPath(node, "code").asText();
    switch (code) {
      case "ok":
        return SetPreferenceCommandResponse.ok();
      case "error":
        String message = JSON_PARSER.findExistingPath(node, "message").asText();
        return SetPreferenceCommandResponse.error(message);
      default:
        throw new RefineException(
            "Failed to parse the response from the set preference operation for property: %s",
            property);
    }
  }

  /**
   * Builder for {@link SetPreferenceCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class Builder {

    private String property;
    private Object value;
    private String token;

    public Builder setProperty(String property) {
      this.property = property;
      return this;
    }

    public Builder setValue(Object value) {
      this.value = value;
      return this;
    }

    public Builder setToken(String token) {
      this.token = token;
      return this;
    }

    /**
     * Builds the {@link SetPreferenceCommand}.
     *
     * @return a command
     */
    public SetPreferenceCommand build() {
      Validate.notBlank(property, "Missing 'property' argument");
      Validate.notNull(value, "Missing 'value' argument");
      Validate.notBlank(token, "Missing CSRF token");
      return new SetPreferenceCommand(property, value, token);
    }
  }
}
