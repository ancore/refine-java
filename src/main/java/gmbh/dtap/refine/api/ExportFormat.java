package gmbh.dtap.refine.api;

/**
 * This enum is used to specify the format of an export file.
 *
 * @since 0.1.1
 */
public enum ExportFormat {

   CSV("csv"), TSV("tsv"), XLS("xls"), XLSX("xlsx"), ODS("ods"), HTML("html");

   private final String format;

   ExportFormat(String format) {
      this.format = format;
   }

   /**
    * Returns the textual value as expected by OpenRefine.
    *
    * @return the value to submit
    * @since 0.1.1
    */
   public String getFormat() {
      return format;
   }
}
