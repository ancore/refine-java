package gmbh.dtap.refine.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gmbh.dtap.refine.api.UploadOptions;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.util.Args.notNull;

/**
 * A minimal implementation of {@link UploadOptions}.
 *
 * @since 0.1.3
 */
public class KeyValueUploadOptions implements UploadOptions {

   private final ObjectMapper objectMapper = new ObjectMapper();
   private final Map<String, Object> options = new HashMap<>();

   private KeyValueUploadOptions() {
   }

   /**
    * Factory method for empty options.
    *
    * @return the empty instance
    * @since 0.1.3
    */
   public static KeyValueUploadOptions create() {
      return new KeyValueUploadOptions();
   }

   /**
    * Factory method with one string key/value pair.
    *
    * @param key   the key
    * @param value the value
    * @return the instance containing one option
    * @since 0.1.3
    */
   public static KeyValueUploadOptions create(String key, String value) {
      return KeyValueUploadOptions.create().with(key, value);
   }

   /**
    * Factory method with one number key/value pair.
    *
    * @param key   the key
    * @param value the value
    * @return the instance containing one option
    * @since 0.1.3
    */
   public static KeyValueUploadOptions create(String key, Number value) {
      return KeyValueUploadOptions.create().with(key, value);
   }

   /**
    * Factory method with one boolean key/value pair.
    *
    * @param key   the key
    * @param value the value
    * @return the instance containing one option
    * @since 0.1.3
    */
   public static KeyValueUploadOptions create(String key, Boolean value) {
      return KeyValueUploadOptions.create().with(key, value);
   }

   /**
    * Adds a string option.
    *
    * @param key   the key
    * @param value the value
    * @return the same instance for fluent access
    */
   public KeyValueUploadOptions with(String key, String value) {
      checkedPut(key, value);
      return this;
   }

   /**
    * Adds a number option.
    *
    * @param key   the key
    * @param value the value
    * @return the same instance for fluent access
    */
   public KeyValueUploadOptions with(String key, Number value) {
      checkedPut(key, value);
      return this;
   }

   /**
    * Adds a boolean option.
    *
    * @param key   the key
    * @param value the value
    * @return the same instance for fluent access
    */
   public KeyValueUploadOptions with(String key, Boolean value) {
      checkedPut(key, value);
      return this;
   }

   @Override
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
