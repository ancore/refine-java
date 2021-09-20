# Ontotext OntoRefine Client Library

## Version 1.1.0

 - Fixed the issue where the export operation command resulted in JSON, which cannot be used directly in apply operation command as it was containing additional tags and wrapping
   of the main operation objects, which should be returned as result.
 - Added additional required parameter to the export RDF command called `format`. It is used to build `Accept` header for the request that is sent to the OntoRefine tool. The
   header is used to show in what RDF format should be returned the result from the command. For example `Turtle`, `Turtle-Star`, `N-Triples`, `RDF/XML`, etc.


## Version 1.0.0

### Initial work

 - The initial work for the library is done by [David Santeodoro Camisón](https://github.com/dsanteodoro) in [refine-java](https://github.com/ancore/refine-java), which we forked.
   Our intent is to build a CLI tool for the [OntoRefine](https://graphdb.ontotext.com/documentation/free/loading-data-using-ontorefine.html) and distribute it as open source
   project. The main reason for our decision to fork the library is that there are some specifics in OntoRefine, which will not be suitable for common library such as the original.

### Changes

 - Updated the versions of all dependencies that are used in the project.
 - Cleaned up some of dependencies that were redundant or unused.
 - Added checkstyle plugin, which will report problems with the code style on build.
 - Added dependency check plugin in order to track any security vulnerabilities.
 - Changed the used junit version and updated all of the tests.
 - Removed the github maven workflow as it is still unclear how and where we are going to build the deployment.
 - Renamed the packages.
 - Reformatted the source code in order to make it compatible with the checkstyle that will be used.
