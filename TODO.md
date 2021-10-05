# TODOs

## Critical


## Major

- Add authentication functionality

## Minor

- Change the building of the Refine instance address. Instead of URL, we can use URI and directly provide it to the request builders
- Refactor the export command to implement the new definition.
- Refactor the project metadata retrieving command to implement the new definition.
- Think of a way to separate the commands in the `RefineCommands` interface by some common classifier. For example `project`, `reconciliation`, `preferences`, etc.
- Remove the unit tests, which are duplicating the integration tests as there are redundant. We need to use unit tests only for commands input verification or error handling.
