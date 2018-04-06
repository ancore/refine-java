package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.RefineProject;
import gmbh.dtap.refine.api.UploadFormat;
import gmbh.dtap.refine.api.UploadOptions;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;
import static org.apache.http.util.Asserts.notNull;

/**
 * This class implements a {@link RefineClient}.
 * <p>
 * Please note that the default used {@link HttpClient} does not support multi-threading.
 * <p>
 * This implementation may be changed significantly as more API functions will be added.
 *
 * @since 0.1.0
 */
public class MinimalRefineClient implements RefineClient {

   private static final Charset charset = Charset.forName("UTF-8");

   private final URL baseUrl;
   private final HttpClient httpClient;

   public MinimalRefineClient(URL baseUrl) {
      notNull(baseUrl, "baseUrl");
      this.baseUrl = baseUrl;
      this.httpClient = HttpClients.createDefault();
   }

   public MinimalRefineClient(URL baseUrl, HttpClient httpClient) {
      notNull(baseUrl, "baseUrl");
      notNull(httpClient, "httpClient");
      this.baseUrl = baseUrl;
      this.httpClient = httpClient;
   }

   @Override
   public RefineProject createProject(String name, File file) throws IOException {
      return createProject(name, file, null, null);
   }

   @Override
   public RefineProject createProject(String name, File file, UploadFormat format, UploadOptions options) throws IOException {
      URL url = new URL(baseUrl, "/command/core/create-project-from-upload");

      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      if (format != null) {
         multipartEntityBuilder.addTextBody("format", format.getFormat(),
               TEXT_PLAIN.withCharset(charset));
      }
      if (options != null) {
         multipartEntityBuilder.addTextBody("options", options.asJson(),
               APPLICATION_JSON.withCharset(charset));
      }

      HttpEntity entity = multipartEntityBuilder
            .addBinaryBody("project-file", file)
            .addTextBody("project-name", name, TEXT_PLAIN.withCharset(charset))
            .build();

      HttpUriRequest request = RequestBuilder
            .post(url.toString())
            .setHeader("Accept", "application/json")
            .setEntity(entity)
            .build();

      String location = httpClient.execute(request, new LocationResponseHandler());
      String id = substringAfterLast(location, "=");
      return new MinimalRefineProject(id, name, new URL(location));
   }

   @Override
   public void deleteProject(String id) throws IOException {
      URL url = new URL(baseUrl, "/command/core/delete-project");

      List<NameValuePair> form = new ArrayList<>();
      form.add(new BasicNameValuePair("project", id));
      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

      HttpUriRequest request = RequestBuilder
            .post(url.toString())
            .setHeader("Accept", "application/json")
            .setEntity(entity)
            .build();

      httpClient.execute(request, new JsonResponseHandler());
   }

   @Override
   public void close() throws Exception {
      if (httpClient instanceof Closeable) {
         ((Closeable) httpClient).close();
      }
   }

   @Override
   public String toString() {
      return "MinimalRefineClient{" +
            "baseUrl=" + baseUrl +
            '}';
   }
}
