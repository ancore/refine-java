# Ontotext OntoRefine Client Library

## Version 1.7

### New

### Changes

### Bug fixes


## Version 1.6.2

### New

 - Introduced additional return types for the SPARQL based RDF export. Previously the result was returned only as ``String``. There are cases, where the produced RDF
   is quite large and if there is no enough memory to for buffering, the command throws OOM exception, while consuming the stream. With the additional types the users
   can avoid that problem by writing the stream in the file or directly consuming it from the response object.
   The return types are also applied for the default command for RDF export, which uses the mapping JSON from the generated from the Mapping UI.
   The users can control the return type by providing different ``ResultType`` to the ``ResultFormat#as(ResultType)``. The default is ``STRING`` to keep the
   library compatible and to ease the migration effort.

### Changes

 - All stream IO operations are now done in ``UTF-8``. There are some utility libraries that have different default charset. To keep the library consistent and
   the behavior predictable, we explicitly set the charset to ``UTF-8``.
 - All third party and utility libraries are not updated to the latest version. There were issues with the artifact deployment so in order to solve it some of the
   libraries had to be updated, but in the process we ended up updating all.
 - Updated the versions of the actions used in the CI and Release processes.

### Bug fixes

 - There were places, where some of the streams were not closed properly, which may cause memory leaks in specific cases. To ensure that the resources are handled
   correctly, we've updated the logic to explicitly close all of the streams that we touch.


## Version 1.6.1

### Bug fixes

 - Changed the mechanism of encoding for the content of the entity, which represents the SPARQL conversion query. Apparently there are characters that are not handled
   correctly by the ``java.net.URLEncoder``. Now we are going to just use ``UrlEncodedFormEntity``, which handles the encoding in correct way.


## Version 1.6

### New

 - Move the project to Java 11. Initially it was on 11, but after a while it was downgraded to 8, because of internal projects that were still developed on 8. Now we can
   proceed with the initial plan.

### Changes

 - Updated the version of the ``jackson-databind`` library to the latest. The previous is detected as vulnerable by the security scans. 

### Bug fixes

 - Fixed a major bug in the `SparqlBasedRdfExportCommand`. The problem was that the payload for the request (the actual SPARQL query) was not encoded, which in
   different cases caused an execution of malformed query. 


## Version 1.5

### New

 - Introduced new command for RDF data export. Instead of a mapping, the command takes advantage SPARQL API of the GraphDB and executed CONSTRUCT query. The query itself
   contains the data source that should be transformed, in this case that is a OntoRefine project.
   In order to make the functionality reusable, in other words executable for multiple projects, the query which is passed to the command contains placeholder for the
   refine project. This allows multiple execution of the defined query over projects with similar data structure.
   The command has one limitation that comes from the GraphDB itself, it requires existence of a repository in order to use the SPARQL API, no matter that it uses refine
   project as data source.
 - Introduces a way to execute integration tests over secured GraphDB. The functionality is implemented in the `IntegrationTest` class, which spawns the GraphDB instance
   used for the tests. The mechanism allow so enable/disable the security on the fly by creating an refine client instance, which uses credentials and another that doesn't. 

### Changes

 - Updated the GraphDB version for the integration tests to 10.0.0-M1 in order to take advantage of the new strategy for the product, which does not require licensing for
   the core functionalities of the product.
 - Added repository via configuration file provisioning to the docker environment, for the integration tests. The name of the repository is `integration-tests` and it is
   created, when the GraphDB is initialized.
 - Renamed the `ExportRdfCommand` to `DefaultExportRdfCommand`. This is a breaking change for implementations that refer to the concrete command class.

### Bug fixes

 - Added logic which allow usage of the RDF mapping JSON in different format in the export RDF and apply operations commands. It fixes usability issue related to the
   extraction of the RDF mapping from different places in the user interface in the OntoRefine or Mapping UI.


## Version 1.4

### New

 - Enhanced the `RefineClients` to allow creation of different types of `RefineClient` instances. Now it can produce security aware client, which accepts credentials
   provider allowing to execute commands over secured Refine tools. Additionally there is an option, which allows full customization over the internal HTTP client that
   will be used by the `RefineClient` instance.

### Changes

 - Deprecated `RefineClients#create` method in favor of `RefineClients#standard`. Now that there are more methods in `RefineClients`, `create` doesn't seems
   appropriate as it doesn't provide context for the type of the produced client.

### Bug fixes

 - Fixed the method `setOptions` in `ExportRowsCommand#Builder` to follow the fluent pattern.
 - Reverted the wrongly committed changes to the interface implementation of the `RefineClient`.
 - Fixed an issue with the `ExportRdfCommand` request entity. If the initial request fails, it is retried due to the retry policy of the client. However the entity
   contains stream, which has been closed, which causes the retry to also fail without obvious reason. The simplest fix was to wrap the `BasicHttpEntity` in
   `BufferedHttpEntity`, which is always repeatable.


## Version 1.3

### New

 - Removed the licensing notice from the Java classes. The reason for doing so is that the current version is distributed with general Apache License Version 2.0. Furthermore
   there were licensing notices for MIT, Apache, etc. in different classes throughout the library.
 - Introduced enumeration for the engines that can be used for the export rows command. Updated the affect code to comply with the new type.
 - Added property of type `Options` to the `ExportRowsCommand`, which allows provisioning of additional configuration for the export processes. Information on which are the
   available configurations for the options property may be found
   [here](https://github.com/OpenRefine/OpenRefine/blob/master/main/src/com/google/refine/exporters/CsvExporter.java#L65).
 - Added new configuration class for the `ExportRowsCommand` called `AdditionalExportConfigs`. It will contain additional options for controlling different aspects of the
   export process. The reason for using different class instead of `Options` is that the handling of these options will be implemented in the library, not in the refine tool
   itself. Currently there is only one option, which allows truncating of the last line in the result files after the export process is completed. It will be mainly used by
   `*SV` exports to keep the correct rows count. 

### Changes

 - `UploadOptions` is renamed to only `Options`. The intention behind the change is for the interface to become more abstract and to be used as general type for providing
   additional information to the different commands.
 - Moved the `ExportRowsCommand` in its own package in order to keep the structure of the project consistent. Updated the command to comply with the new definition of the
   commands.
 - Changed the object that is used for creation of the full endpoint address from `URL` to `URI`, which can be passed directly to the request builders without the need of
   converting it to `String`. 


## Version 1.2

### New

 - Updated the README document of the repository to represent the current state of the library.
 - Added DEV documents, which contains description of the implementation steps for the commands. Basically it provides information how contributors may introduce new commands.
 - Added ARCHITECTURE document, which provides a simple overview of the high level components and the how they interact with each other.
 - Added SPEC document intended to contain the specification for the different functionalities which the library will provide.
 - Added infrastructure for integration testing using real instance of `OntoRefine` tool by spawning `GraphDB` using `Docker` image with specific version. The container
   management is done using the test library [Testcontainers](https://www.testcontainers.org/).
 - Added integration tests for the currently implemented commands. The tests are representing execution of specific scenarios, which are using different commands to be completed.

### Changes

 - Changed the interface which `RefineClient` implements from `AutoCloseable` to `Closeable`.


## Version 1.1.0

### New

 - Introduced new command which allows registration of reconciliation service/s as standard services. The new services will be added in the preferences properties of the Refine
   tool in order to make them available in the reconciliation operations.
 - Introduced commands for setting and retrieving of preference for specific property. They are both used as composite commands for the command that registers new reconciliation
   service.
 - Introduced new command which allows retrieval of the models of specific project. It provides temporary workaround for the issue with the RDF mapping, where after update it won't
   be synchronized with the JSON configuration for the operations done over the project. We were using the mapping from the operations in the RDF exporting. 

### Changes

 - Added additional required parameter to the export RDF command called `format`. It is used to build `Accept` header for the request that is sent to the OntoRefine tool. The
   header is used to show in what RDF format should be returned the result from the command. For example `Turtle`, `Turtle-Star`, `N-Triples`, `RDF/XML`, etc.
   
### Bug fixes

 - Fixed the issue where the export operation command resulted in JSON, which cannot be used directly in apply operation command as it was containing additional tags and wrapping
   of the main operation objects, which should be returned as result.


## Version 1.0.0

### Initial work

 - The initial work for the library is done by [Andreas Cordsen](https://github.com/ancore) in [refine-java](https://github.com/ancore/refine-java), which we forked.
   Our intent is to build a client library for the [OntoRefine](https://graphdb.ontotext.com/documentation/free/loading-data-using-ontorefine.html) and distribute it as a open
   source project. The main reason for our decision to fork the library is that there are some specifics in OntoRefine, which will not be suitable for common library such as the
   original.

### Changes

 - Updated the versions of all dependencies that are used in the project.
 - Cleaned up some of dependencies that were redundant or unused.
 - Added checkstyle plugin, which will report problems with the code style on build.
 - Added dependency check plugin in order to track any security vulnerabilities.
 - Changed the used junit version and updated all of the tests.
 - Removed the github maven workflow as it is still unclear how and where we are going to build the deployment.
 - Renamed the packages.
 - Reformatted the source code in order to make it compatible with the checkstyle that will be used.
 - Added few new commands like, reconcile, operations retrieval, processes retrieval, etc.
 - Added CI and CD pipelines achieved via GitHub actions. The released artifact will be deployed in the Ontotext Public Maven Repository
 - Separated the different commands in dedicated packages in order to make the orientation in the code base easy.
 - Introduced few new interfaces and abstractions which should accelerate the implementation of a new commands.
 - Added more tests.
