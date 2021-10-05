# Ontotext OntoRefine Client Library

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
