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

package com.ontotext.refine.client;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.impl.client.HttpClients;

public interface RefineClients {

  static RefineClient create(String url) throws MalformedURLException {
    // TODO configure some sensible timeouts and other configurations
    return new RefineClient(new URL(url), HttpClients.createDefault());
  }
}