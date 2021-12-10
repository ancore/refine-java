package com.ontotext.refine.client.command;

import com.ontotext.refine.client.ProjectMetadata;

public class GetProjectMetadataResponse {

  private final ProjectMetadata projectMetadata;

  public GetProjectMetadataResponse(ProjectMetadata projectMetadata) {
    this.projectMetadata = projectMetadata;
  }

  public ProjectMetadata getProjectMetadata() {
    return projectMetadata;
  }

  @Override
  public String toString() {
    return "GetProjectMetadataResponse{" + "projectMetadata=" + projectMetadata + '}';
  }
}
