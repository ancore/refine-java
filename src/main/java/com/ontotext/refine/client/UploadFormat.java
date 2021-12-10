package com.ontotext.refine.client;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * This enum is used to specify the format of an upload file when creating a {@link RefineProject}.
 * This is recommended if the file extension does not indicate the format clearly.
 */
public enum UploadFormat {

  /**
   * line-based text file.
   */
  LINE_BASED("text/line-based"),

  /**
   * CSV / TSV / separator-based file The separator to be used has to be submitted with
   * {@link UploadOptions}.
   */
  SEPARATOR_BASED("text/line-based/*sv"),

  /**
   * fixed-width field text file.
   */
  FIXED_WIDTH_FIELD("text/line-based/fixed-width"),

  /**
   * Excel file.
   */
  EXCEL("binary/text/xml/xls/xlsx"),

  /**
   * JSON_PARSER file.
   */
  JSON("text/json"),

  /**
   * XML file.
   */
  XML("text/xml");

  private final String value;

  private UploadFormat(String value) {
    this.value = value;
  }

  /**
   * Resolvers the {@link UploadFormat} by value.
   *
   * @param value which corresponds to {@link UploadFormat}
   * @return {@link UploadFormat} or throws an exception if there isn't format with the provided
   *         value
   */
  public static UploadFormat resolve(String value) {
    return Arrays.stream(values())
        .filter(format -> format.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("No value present"));
  }

  /**
   * Returns the textual value as expected by OpenRefine.
   *
   * @return the value to submit
   */
  public String getValue() {
    return value;
  }
}
