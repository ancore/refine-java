package gmbh.dtap.refine.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;

/**
 * A command to create a project.
 */
public class CreateProjectCommand {

   private static final Charset charset = Charset.forName("UTF-8");

   private String name;
   private File file;
   private UploadFormat format;
   private UploadOptions options;

   /**
    * Sets the project name.
    *
    * @param name the project name
    * @return the command for fluent usage
    */
   public CreateProjectCommand name(String name) {
      this.name = name;
      return this;
   }

   /**
    * Sets the file containing the data to upload.
    *
    * @param file the file containing the data to upload
    * @return the command for fluent usage
    */
   public CreateProjectCommand file(File file) {
      this.file = file;
      return this;
   }

   /**
    * Sets the optional upload format.
    *
    * @param format the optional upload format
    * @return the command for fluent usage
    */
   public CreateProjectCommand format(UploadFormat format) {
      this.format = format;
      return this;
   }

   /**
    * Sets the optional options.
    *
    * @param options the optional options
    * @return the command for fluent usage
    */
   public CreateProjectCommand options(UploadOptions options) {
      this.options = options;
      return this;
   }

   /**
    * Executes the command after validation.
    *
    * @param client the client to execute the command with
    * @return the result of the command
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the server responses with an error or is not understood
    */
   public CreateProjectResponse execute(RefineClient client) throws IOException {
      notNull(name, "name");
      notEmpty(name, "name");
      notNull(file, "file");

      URL url = client.createUrl("/command/core/create-project-from-upload");

      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      if (format != null) {
         multipartEntityBuilder.addTextBody("format", format.getValue(),
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
            .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
            .setEntity(entity)
            .build();

      return client.execute(request, new CreateProjectResponseHandler());
   }
}
