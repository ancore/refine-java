package com.ontotext.refine.client.command;

import java.io.File;

public class ExportRowsResponse {

  private File file;

  ExportRowsResponse(File file) {
    this.file = file;
  }

  public File getFile() {
    return file;
  }

}
