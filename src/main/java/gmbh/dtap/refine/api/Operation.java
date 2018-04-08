package gmbh.dtap.refine.api;

/**
 * Operations can be applied to a refine project with {@link RefineClient#applyOperations(String, Operation...)}.
 *
 * @since 0.1.3
 */
public interface Operation {

   /**
    * Returns the operation as JSON in the format expected by OpenRefine.
    *
    * @return the operation as JSON
    * @since 0.1.3
    */
   String asJson();
}
