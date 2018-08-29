package gmbh.dtap.refine.client;

/**
 * Operations can be applied to a refine project with {@link ApplyOperationsCommand}.
 */
public interface Operation {

   /**
    * Returns the operation as JSON_PARSER in the format expected by OpenRefine.
    *
    * @return the operation as JSON_PARSER
    */
   String asJson();
}
