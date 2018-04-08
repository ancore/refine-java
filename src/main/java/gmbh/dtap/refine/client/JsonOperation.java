package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.Operation;

/**
 * A minimal implementation of {@link Operation}.
 *
 * @since 0.1.4
 */
public class JsonOperation implements Operation {

   private final String json;

   private JsonOperation(String json) {
      this.json = json;
   }

   /**
    * Factory method that crates an instance from JSON.
    * The JSON has to be in the format expected by OpenRefine.
    *
    * @param json the JSON document
    * @return the engine instance
    * @since 0.1.4
    */
   public static Operation from(String json) {
      return new JsonOperation(json);
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
