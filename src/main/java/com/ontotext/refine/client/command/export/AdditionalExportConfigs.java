package com.ontotext.refine.client.command.export;

/**
 * Provides additional export configuration that are implemented in the current library, not the
 * refine tool itself.
 *
 * @author Antoniy Kunchev
 */
public class AdditionalExportConfigs {

  private boolean truncateFile = false;

  private AdditionalExportConfigs() {
    // use the create method
  }

  /**
   * Creates new instance of {@link AdditionalExportConfigs}.
   *
   * @return new instance for the current class
   */
  public static AdditionalExportConfigs createDefault() {
    return new AdditionalExportConfigs();
  }

  /**
   * Whether the last line of the result file of the {@link ExportRowsCommand} should be removed or
   * not. The default is <code>false</code>.
   *
   * @return <code>true</code> if the line should be removed, <code>false</code> otherwise
   */
  public boolean truncateFile() {
    return truncateFile;
  }

  public AdditionalExportConfigs setTruncateFile(boolean truncateFile) {
    this.truncateFile = truncateFile;
    return this;
  }
}
