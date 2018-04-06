package gmbh.dtap.refine.api;

/**
 * This is added as restrictions when export rows with the {@link RefineClient},
 * e.g. {@code '{ "facets":[], "mode":"row-based" }'.
 *
 * @since 0.1.1
 */
public class Engine {

   private final String json;

   private Engine(String json) {
      this.json = json;
   }

   public static Engine from(String json) {
      return new Engine(json);
   }

   public String asJson() {
      return json;
   }

   @Override
   public String toString() {
      return asJson();
   }
}
