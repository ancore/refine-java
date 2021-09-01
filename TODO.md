# TODOs

## Critical

- Update or rewrite the README file.
- Discuss the deployment and the integration with the GraphDB

## Major

- Add authentication functionality

## Minor

- Change the building of the Refine instance address. Instead of URL, we can use URI and directly provide it to the request builders
- Refactor the export command to implement the new definition and provide CLI for it.
- Refactor the project metadata retrieving command to implement the new definition and provide CLI for it.
- Think of a way to separate the commands in the `RefineCommands` interface by some common classifier. For example `project`, `reconciliation`, `preferences`, etc. 
