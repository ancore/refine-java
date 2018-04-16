# OpenRefine API Java Client 

This library is a client implementation for the [OpenRefine API](https://github.com/OpenRefine/OpenRefine/wiki/OpenRefine-API).

Supported functions are

* Create project
* Delete project
* Export rows
* Get all project metadata
* Apply operations
* Expression Preview

Currently missing functions are

* Check status of async processes (see https://github.com/dtap-gmbh/refine-java/issues/12)

## Usage

### Create and Delete Project
```java
String url = "http://localhost:3333/";
File file = ...;
   
try (RefineClient client = RefineClients.create(url)) {
         RefineProjectLocation location = client.createProject("Project name", file);
         // ...
         client.deleteProject(location.getId());
}
```

### Create Project and Preview Expression
```java
try (RefineClient client = RefineClients.create(url)) {
   RefineProjectLocation location = client.createProject("Addresses", file);
   List<String> expressionPreviews = client.expressionPreview(location.getId(),
         4, new long[]{0, 1},
         "grel:toLowercase(value);", false, 0);
   System.out.println(expressionPreviews);
   client.deleteProject(location.getId());
}
```

### Fluent Request Execuctor
```java
try (RefineClient client = RefineClients.create(url)) {

   RefineProjectLocation location = RequestExeuctor
      .createProject()
          .name("Addresses")
          .file(file)
          .execute(client);

   List<String> expressionPreviews = RequestExeuctor
      .expressionPreview()
         .project(location)
         .cellIndex(4)
         .rowIndices(0, 1)
         .expression("grel:toLowercase(value)")
         .repeat(false)
         .repeatCount(0)
         .execute(client);

   System.out.println(expressionPreviews);
}
```

More examples can be found in `gmbh.dtap.refine.Usage`.

## Credits

Copyright (c) 2018 DTAP GmbH

Please refer to the LICENSE file for details.
