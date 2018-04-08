package gmbh.dtap.refine.api;

/**
 * This interface defines the import options metadata of a {@link RefineProject}.
 *
 * @since 0.1.2
 */
public interface ImportOptionMetadata {

   /**
    * @since 0.1.2
    */
   boolean isStoreBlankRows();

   /**
    * @since 0.1.2
    */
   boolean isIncludeFileSources();

   /**
    * @since 0.1.2
    */
   int getSkipDataLines();

   /**
    * @since 0.1.2
    */
   boolean isGuessCellValueTypes();

   /**
    * @since 0.1.2
    */
   int getHeaderLines();

   /**
    * @since 0.1.2
    */
   int getIgnoreLines();

   /**
    * @since 0.1.2
    */
   boolean isProcessQuotes();

   /**
    * @since 0.1.2
    */
   String getFileSource();

   /**
    * @since 0.1.2
    */
   String getProjectName();

   /**
    * @since 0.1.2
    */
   String getSeparator();

   /**
    * @since 0.1.2
    */
   boolean isStoreBlankCellsAsNulls();
}
