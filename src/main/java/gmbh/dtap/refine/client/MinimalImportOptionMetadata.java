package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.ImportOptionMetadata;

/**
 * A minimal implementation of {@link ImportOptionMetadata}.
 *
 * @since 0.1.2
 */
class MinimalImportOptionMetadata implements ImportOptionMetadata {

   private boolean storeBlankRows;
   private boolean includeFileSources;
   private int skipDataLines;
   private boolean guessCellValueType;
   private int headerLines;
   private int ignoreLines;
   private boolean processQuotes;
   private String fileSource;
   private String projectName;
   private String separator;
   private boolean storeBlankCellsAsNulls;

   @Override
   public boolean isStoreBlankRows() {
      return storeBlankRows;
   }

   void setStoreBlankRows(boolean storeBlankRows) {
      this.storeBlankRows = storeBlankRows;
   }

   @Override
   public boolean isIncludeFileSources() {
      return includeFileSources;
   }

   void setIncludeFileSources(boolean includeFileSources) {
      this.includeFileSources = includeFileSources;
   }

   @Override
   public int getSkipDataLines() {
      return skipDataLines;
   }

   void setSkipDataLines(int skipDataLines) {
      this.skipDataLines = skipDataLines;
   }

   @Override
   public boolean isGuessCellValueTypes() {
      return guessCellValueType;
   }

   void setGuessCellValueType(boolean guessCellValueType) {
      this.guessCellValueType = guessCellValueType;
   }

   @Override
   public int getHeaderLines() {
      return headerLines;
   }

   void setHeaderLines(int headerLines) {
      this.headerLines = headerLines;
   }

   @Override
   public int getIgnoreLines() {
      return ignoreLines;
   }

   void setIgnoreLines(int ignoreLines) {
      this.ignoreLines = ignoreLines;
   }

   @Override
   public boolean isProcessQuotes() {
      return processQuotes;
   }

   void setProcessQuotes(boolean processQuotes) {
      this.processQuotes = processQuotes;
   }

   @Override
   public String getFileSource() {
      return fileSource;
   }

   void setFileSource(String fileSource) {
      this.fileSource = fileSource;
   }

   @Override
   public String getProjectName() {
      return projectName;
   }

   void setProjectName(String projectName) {
      this.projectName = projectName;
   }

   @Override
   public String getSeparator() {
      return separator;
   }

   void setSeparator(String separator) {
      this.separator = separator;
   }

   @Override
   public boolean isStoreBlankCellsAsNulls() {
      return storeBlankCellsAsNulls;
   }

   void setStoreBlankCellsAsNulls(boolean storeBlankCellsAsNulls) {
      this.storeBlankCellsAsNulls = storeBlankCellsAsNulls;
   }

   @Override
   public String toString() {
      return "MinimalImportOptionMetadata{" +
            "storeBlankRows=" + storeBlankRows +
            ", includeFileSources=" + includeFileSources +
            ", skipDataLines=" + skipDataLines +
            ", guessCellValueType=" + guessCellValueType +
            ", headerLines=" + headerLines +
            ", ignoreLines=" + ignoreLines +
            ", processQuotes=" + processQuotes +
            ", fileSource='" + fileSource + '\'' +
            ", projectName='" + projectName + '\'' +
            ", separator='" + separator + '\'' +
            ", storeBlankCellsAsNulls=" + storeBlankCellsAsNulls +
            '}';
   }
}
