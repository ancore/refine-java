package com.ontotext.refine.client.command.create;

import static org.apache.commons.lang3.StringUtils.substringAfterLast;

import java.net.URL;


/**
 * Holds the response data from create project command execution.
 */
public class CreateProjectResponse {

  private final URL location;

  CreateProjectResponse(URL location) {
    this.location = location;
  }

  public URL getLocation() {
    return location;
  }

  public String getProjectId() {
    return substringAfterLast(location.getQuery(), "=");
  }

  @Override
  public String toString() {
    return "CreateProjectResponse{" + "location=" + location + '}';
  }
}
