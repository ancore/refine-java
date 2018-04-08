package gmbh.dtap.refine.api;

/**
 * This is added as restrictions when export rows with the {@link RefineClient},
 * e.g. {@code '{ "facets":[], "mode":"row-based" }'.
 *
 * @since 0.1.1
 */
public interface Engine {

   /**
    * Returns the options as JSON in the format expected by OpenRefine.
    *
    * @return the options as JSON
    * @since 0.1.1
    */
   String asJson();
}
