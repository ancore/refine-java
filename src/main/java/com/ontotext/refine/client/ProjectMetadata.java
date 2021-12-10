package com.ontotext.refine.client;

public class ProjectMetadata {

  private String name;
  private long rowCount;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getRowCount() {
    return rowCount;
  }

  public void setRowCount(long rowCount) {
    this.rowCount = rowCount;
  }

  @Override
  public String toString() {
    return "ProjectMetadata{" + "name='" + name + '\'' + ", rowCount=" + rowCount + '}';
  }
}
