package gmbh.dtap.refine.api;

/**
 * This enum is used to specify the format of an export file.
 *
 * @since 0.1.1
 */
public enum ExportFormat {

   csv("csv"), tsv("tsv"), xls("xls"), xlsx("xlsx"), ods("ods"), html("html");

   private String format;

   ExportFormat(String format) {
      this.format = format;
   }

   public String getFormat() {
      return format;
   }
}
