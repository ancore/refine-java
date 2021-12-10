package com.ontotext.refine.client;

/**
 * This interface defines the import options metadata of a {@link RefineProject}.
 */
public interface ImportOptionMetadata {

  boolean isStoreBlankRows();

  boolean isIncludeFileSources();

  int getSkipDataLines();

  boolean isGuessCellValueTypes();

  int getHeaderLines();

  int getIgnoreLines();

  boolean isProcessQuotes();

  String getFileSource();

  String getProjectName();

  String getSeparator();

  boolean isStoreBlankCellsAsNulls();
}
