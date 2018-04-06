package gmbh.dtap.refine.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.util.Asserts.notNull;

/**
 * The {@link UploadOptions} can be added when creating a {@link RefineProject} with the {@link RefineClient}.
 * Options are used to give additional information on some {@link UploadFormat}s.
 *
 * @since 0.1.0
 */
public class UploadOptions {

   private final ObjectMapper objectMapper = new ObjectMapper();
   private final Map<String, Object> options = new HashMap<>();

   public static UploadOptions create() {
      return new UploadOptions();
   }

   public static UploadOptions create(String key, String value) {
      return UploadOptions.create().with(key, value);
   }

   public static UploadOptions create(String key, Number value) {
      return UploadOptions.create().with(key, value);
   }

   public static UploadOptions create(String key, Boolean value) {
      return UploadOptions.create().with(key, value);
   }

   public UploadOptions with(String key, String value) {
      checkedPut(key, value);
      return this;
   }

   public UploadOptions with(String key, Number value) {
      checkedPut(key, value);
      return this;
   }

   public UploadOptions with(String key, Boolean value) {
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
