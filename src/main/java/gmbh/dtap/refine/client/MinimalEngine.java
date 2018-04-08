package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.Engine;
import gmbh.dtap.refine.api.RefineClient;

/**
 * This is added as restrictions when export rows with the {@link RefineClient},
 * e.g. {@code '{ "facets":[], "mode":"row-based" }'.
 *
 * @since 0.1.3
 */
public class MinimalEngine implements Engine {

   private final String json;

   private MinimalEngine(String json) {
      this.json = json;
   }

   public static MinimalEngine from(String json) {
      return new MinimalEngine(json);
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
