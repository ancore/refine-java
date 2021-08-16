/*
 * Copyright 2019 DTAP GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
