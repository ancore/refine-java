package gmbh.dtap.refine.api;

/**
 * This enum is used to specify the format of an upload file when creating a {@link RefineProject}.
 * This is recommended if the file extension does not indicate the format clearly.
 *
 * @since 0.1.0
 */
public enum UploadFormat {

   /**
    * line-based text file
    */
   LINE_BASED("text/line-based"),

   /**
    * CSV / TSV / separator-based file
    * The separator to be used has to be submitted with {@link UploadOptions}.
    */
   SEPARATOR_BASED("text/line-based/*sv'"),

   /**
    * fixed-width field text file
    */
   FIXED_WIDTH_FIELD("text/line-based/fixed-width"),

   /**
    * Excel file
    */
   EXCEL("binary/text/xml/xls/xlsx"),

   /**
    * JSON file
    */
   JSON("text/json"),

   /**
    * XML file
    */
   XML("text/xml");

   private final String value;

   UploadFormat(String value) {
      this.value = value;
   }

   /**
    * Returns the textual value as expected by OpenRefine.
    *
    * @return the value to submit
    * @since 0.1.3
    */
   public String getValue() {
      return value;
   }
}
