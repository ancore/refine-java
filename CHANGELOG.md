# Ontotext OntoRefine CLI

## Version 0.1.0

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
