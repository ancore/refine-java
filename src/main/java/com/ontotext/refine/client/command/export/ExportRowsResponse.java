package com.ontotext.refine.client.command.export;

import java.io.File;

/**
 * Holds the result from the {@link ExportRowsCommand}.
 * 
 * @author Antoniy Kunchev
 */
public class ExportRowsResponse {

  private File file;

  ExportRowsResponse(File file) {
    this.file = file;
  }

  public File getFile() {
    return file;
  }
}
