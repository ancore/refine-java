package gmbh.dtap.refine.api;

/**
 * This enum is used to specify the format of an upload file when creating a {@link RefineProject} with the {@link RefineClient}.
 * This is helpful if the file extension does not indicate the format clearly.
 *
 * @since 0.1.0
 */
public enum UploadFormat {

   /**
    * line-based text file
    */
   lineBased("text/line-based"),

   /**
    * CSV / TSV / separator-based file
    * The separator to be used has to be submitted with {@link UploadOptions}.
    */
   separatorBased("text/line-based/*sv'"),

   /**
    * fixed-width field text file
    */
   fixedWidthField("text/line-based/fixed-width"),

   /**
    * Excel file
    */
   excel("binary/text/xml/xls/xlsx"),

   /**
    * JSON file
    */
   json("text/json"),

   /**
    * XML file
    */
   xml("text/xml");

   private String format;

   UploadFormat(String format) {
      this.format = format;
   }

   public String getFormat() {
      return format;
   }
}
