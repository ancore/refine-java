[![Build Status](https://github.com/dtap-gmbh/refine-java/actions/workflows/maven.yml/badge.svg)](https://github.com/dtap-gmbh/refine-java) 

# OpenRefine API Java Client 

This library is a client implementation for the [OpenRefine API](https://github.com/OpenRefine/OpenRefine/wiki/OpenRefine-API).

Supported functions are

* Apply Operations
* Create Project
* Delete Project
* Expression Preview
* Get Version

## Usage

      try (RefineClient client = RefineClients.create("http://localhost:3333")) {

         GetVersionResponse version = RefineCommands
               .getVersion()
               .execute(client);
         System.out.println(version);

         CreateProjectResponse createProjectResponse = RefineCommands
               .createProject()
               .name("Test 1")
               .file(new File("src/test/resources/addresses.csv"))
               .execute(client);
         System.out.println(createProjectResponse);

         Operation operation = JsonOperation.from("{\n" +
               "       \"op\": \"core/column-split\",\n" +
               "       \"description\": \"Split column ID by separator\",\n" +
               "       \"engineConfig\": {\n" +
               "              \"facets\": [],\n" +
               "              \"mode\": \"row-based\"\n" +
               "       },\n" +
               "       \"columnName\": \"ID\",\n" +
               "       \"guessCellType\": true,\n" +
               "       \"removeOriginalColumn\": true,\n" +
               "       \"mode\": \"separator\",\n" +
               "       \"separator\": \"-\",\n" +
               "       \"regex\": false,\n" +
               "       \"maxColumns\": 0\n" +
               "}");
         ApplyOperationsResponse applyOperationsResponse = RefineCommands
               .applyOperations()
               .project(createProjectResponse.getProjectId())
               .operations(operation)
               .execute(client);
         System.out.println(applyOperationsResponse);

         DeleteProjectResponse deleteProjectResponse = RefineCommands
               .deleteProject()
               .project(createProjectResponse.getProjectId())
               .execute(client);
         System.out.println(deleteProjectResponse);
      }
```

## Credits

Copyright (c) 2018 DTAP GmbH

Please refer to the LICENSE file for details.
