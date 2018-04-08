package gmbh.dtap.refine.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.RefineProject;
import gmbh.dtap.refine.api.UploadFormat;
import gmbh.dtap.refine.api.UploadOptions;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.util.Asserts.notNull;

/**
 * These options are added when creating a {@link RefineProject} with the {@link RefineClient}.
 * Options are used to give additional information on some {@link UploadFormat}s.
 *
 * @since 0.1.3
 */
public class MinimalUploadOptions implements UploadOptions {

   private final ObjectMapper objectMapper = new ObjectMapper();
   private final Map<String, Object> options = new HashMap<>();

   public static MinimalUploadOptions create() {
      return new MinimalUploadOptions();
   }

   public static MinimalUploadOptions create(String key, String value) {
      return MinimalUploadOptions.create().with(key, value);
   }

   public static MinimalUploadOptions create(String key, Number value) {
      return MinimalUploadOptions.create().with(key, value);
   }

   public static MinimalUploadOptions create(String key, Boolean value) {
      return MinimalUploadOptions.create().with(key, value);
   }

   public MinimalUploadOptions with(String key, String value) {
      checkedPut(key, value);
      return this;
   }

   public MinimalUploadOptions with(String key, Number value) {
      checkedPut(key, value);
      return this;
   }

   public MinimalUploadOptions with(String key, Boolean value) {
      checkedPut(key, value);
      return this;
   }

   public String asJson() {
      try {
         return objectMapper.writeValueAsString(options);
      } catch (JsonProcessingException e) {
         throw new IllegalArgumentException(e.getMessage(), e);
      }
   }

   private void checkedPut(String key, Object value) {
      notNull(key, "key");
      notNull(value, "value");
      options.put(key, value);
   }

   @Override
   public String toString() {
      return asJson();
   }
}
