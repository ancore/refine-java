package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.Engine;

/**
 * A minimal implementation of {@link Engine}.
 *
 * @since 0.1.3
 */
public class JsonEngine implements Engine {

   private final String json;

   private JsonEngine(String json) {
      this.json = json;
   }

   /**
    * Factory method that crates an instance from JSON.
    * THe JSON has to be in the format expected by OpenRefine.
    *
    * @param json the JSON document
    * @return the engine instance
    * @since 0.1.3
    */
   public static Engine from(String json) {
      return new JsonEngine(json);
   }

   @Override
   public String asJson() {
      return json;
   }

   @Override
   public String toString() {
      return asJson();
   }
}
