[![Java CI with Maven](https://github.com/Ontotext-AD/ontorefine-client/actions/workflows/CI.yaml/badge.svg)](https://github.com/Ontotext-AD/ontorefine-client/actions/workflows/CI.yaml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Ontotext-AD_ontorefine-client&metric=alert_status)](https://sonarcloud.io/dashboard?id=Ontotext-AD_ontorefine-client)


# OntoRefine Client 

A library providing client for execution of different operation in [OntoRefine](https://graphdb.ontotext.com/documentation/free/loading-data-using-ontorefine.html) tool.

Current implementation is inspired by the design and structure of [OpenRefine API Java Client](https://github.com/ancore/refine-java), which we forked and changed to 
suite our needs and requirements for OntoRefine tool. Big thanks to [Andreas Cordsen](https://github.com/ancore), who is the main contributor and author of the forked
repository.

## Purpose

The main purpose of the client is to provide an easy to use API for the OntoRefine tool. This will allow easy integration with other tools or automation of reconciliation
and/or RDFization processes. This will open the possibility for process templating, which on the other hand will accelerate the processing of large amount of similar data.
The intention for the client is to provide the main functionalities that OntoRefine tool offers, where the user should input minimal amount of configurations in order to
complete given operation.

## Installation

```xml
 <dependency>
     <groupId>com.ontotext</groupId>
     <artifactId>ontorefine-client</artifactId>
     <version>1.4.0</version>
 </dependency>
```

If there are any problems related to the download of the packages you need to add additional repository configuration, because the packages are hosted in our own Public
Nexus repository.

```xml
 <repository>
     <id>all-onto</id>
     <name>Ontotext Public Maven</name>
     <url>https://maven.ontotext.com/content/repositories/public</url>
     <releases>
         <enabled>true</enabled>
     </releases>
     <snapshots>
         <enabled>true</enabled>
     </snapshots>
 </repository>
```


## Supported functionalities

The operations that are currently supported:

- Project creation - creates new project in OntoRefine
- Project deletion - deletes existing project in OntoRefine
- Project data export - exports the data of a OntoRefine project
- Exporting project data in RDF format - exports the data of a OntoRefine project in RDF format using the RDF mapping defined with the Mapping UI tool
- Exporting project data in RDF format via SPARQL - exports the data of a OntoRefine project in RDF format using a SPARQL CONSTRUCT query
- Applying of operations over project data - applies set of operation to OntoRefine project. The operations are represented via JSON configuration
- Project operations exporting - exports the operations that are applied to OntoRefine project. The result is in JSON format and can be directly used in different project
- Version retrieval - provides information about the version of the OntoRefine tool
- Project data reconciliation - performs reconciliation of specific column of a OntoRefine project data. The reconciliation can be performed using different reconciliation services
- Guess column types - suggests a type for specific column of a OntoRefine project data
- CSRF token retrieval - retrieves new CSRF token from OntoRefine tool
- Get project processes - retrieves the running processes for specific OntoRefine project, if there are any at the moment of the request
- Get project models - retrieves all models for specific OntoRefine project
- Register additional reconciliation service - allows registration of extra reconciliation services. After registration, the services can be used for reconciliation processes
- Get preference - retrieves specific preference of the OntoRefine tool. The preferences are specified by name
- Set preference - sets value for specific reference 
- (WIP) Expression preview - extracts the expressions used in specific OntoRefine project
- (WIP) Project metadata retrieval - retrieves the metadata for the specific OntoRefine project


## Usage

The basic usage of the library is shown below. The example showcases the scenario, where the user wants to create project, apply operations over the project data, which will
transform the data in some way, export the data after the transformation in order to return result from the processing. As last step in the example, which will be executed
always even if some error occurs, is the clean up of the project.

Following the same approach shown in the example, the users can compose the different commands to create various processing pipelines or expose the different commands through
interface, which is the case with [ontorefine-cli](https://github.com/Ontotext-AD/ontorefine-cli) project.


```java
import com.ontotext.refine.client.command.ExportRowsResponse;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

class Example {

  ExportRowsResponse exportAfterTransformation(File csv, File transformationScript)
      throws Exception {
    String projectId = null;
    RefineClient client = null;

    try {
      client = RefineClients.create("http://graphdb:7200");

      // create project using the provided CSV and get the identifier of the result project
      projectId = RefineCommands.createProject()
          .name(dataset.getName())
          .file(csv)
          .format(UploadFormat.SEPARATOR_BASED)
          .token(getToken(client))
          .build()
          .execute(client)
          .getProjectId();

      // apply the transformation over the project data
      String operationsAsStr =
          IOUtils.toString(transformationScript.toURI(), StandardCharsets.UTF_8);
      Operation operations = JsonOperation.from(operationsAsStr);
      RefineCommands.applyOperations()
          .project(projectId)
          .operations(operations)
          .token(getToken(client))
          .build()
          .execute(client);

      // export the result data as CSV
      return RefineCommands.exportRows()
          .project(projectId)
          .format("csv")
          .token(getToken(client))
          .build()
          .execute(client);
    } finally {
    
      // delete the created project with all of the related data
      RefineCommands.deleteProject()
          .project(projectId)
          .token(getToken(client))
          .build()
          .execute(client);

      // close the client as we are not using the AutoCloseable mechanism with try-with-resources
      IOUtils.closeQuietly(client);
    }
  }

  private String getToken(RefineClient client) throws RefineException {
    return RefineCommands.getCsrfToken().build().execute(client).getToken();
  }
}

```

Although the library is intended to be used in tandem with `OntoRefine` tool, it is based on implementation for
`OpenReine`, so potentially it can be used as middle layer between different applications and `OpenRefine` instances,
where most of the functionalities should work without changes.

Currently we do not intend to support `OpenRefine` as additional tool so we encourage the users to implement their
functionalities using the `OntoRefine` tool.


## Development & Releases

Please checkout the [DEV](DEV.md) document for more information on the topics.


## Architecture Overview

Checkout the [ARCHITECTURE](ARCHITECTURE.md) document.


## Contribution



## License

Please refer to the [LICENSE](LICENSE) file for details.
