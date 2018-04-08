package gmbh.dtap.refine.api;

import gmbh.dtap.refine.RefineClients;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * This interface defines access to the <a href="https://github.com/OpenRefine/OpenRefine/wiki/OpenRefine-API"></a>OpenRefine API</a>.
 * Implementing classes handle requests and responses with an OpenRefine server over HTTP.
 * <p>
 * Instances can be created with {@link RefineClients}.
 * <p>
 * Support for <tt>try-with-resources</tt> is supported with {@link AutoCloseable}.
 *
 * @since 0.1.0
 */
public interface RefineClient extends AutoCloseable {

   /**
    * Creates a project at the OpenRefine server.
    *
    * @param name the project name
    * @param file the file containing the data to upload
    * @return the location of the created refine project
    * @throws IOException in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.0
    */
   RefineProjectLocation createProject(String name, File file) throws IOException;

   /**
    * Creates a project at the OpenRefine server.
    *
    * @param name    the project name
    * @param file    the file containing the data to upload
    * @param format  the format, {@code null} for the refine server to guess
    * @param options the options, {@code null} for none
    * @return the location of the created refine project
    * @throws IOException in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.0
    */
   RefineProjectLocation createProject(String name, File file, UploadFormat format, UploadOptions options) throws IOException;

   /**
    * Deletes a project at the OpenRefine server.
    *
    * @param id the project ID
    * @throws IOException in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.0.0
    */
   void deleteProject(String id) throws IOException;

   /**
    * Exports (downloads) rows from the OpenRefine server.
    *
    * @param id     the project ID
    * @param engine optional restrictions
    * @param format the file format
    * @return the number of bytes written
    * @throws IOException in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.1
    */
   int exportRows(String id, Engine engine, ExportFormat format, OutputStream outputStream) throws IOException;

   /**
    * Returns the metadata of all projects, including project's ID, name and timestamps.
    *
    * @return a list of all available projects, never {@code null}
    * @throws IOException in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.2
    */
   List<RefineProject> getAllProjectMetadata() throws IOException;

   /**
    * Applies operations on the specific project.
    *
    * @param projectId  the project ID
    * @param operations the operations, at least one operation has to be provided
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.3
    */
   void applyOperations(String projectId, Operation... operations) throws IOException;

   /**
    * Returns the statuses of asynchronous processes running on the OpenRefine server.
    *
    * @return the process statuses, never {@code null}
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.3
    */
   List<ProcessStatus> checkStatusOfAsyncProcesses() throws IOException;

   /**
    * Returns the expression preview for the project.
    *
    * @param projectId   the project ID
    * @param cellIndex   the cell/column to execute the expression on
    * @param expression  the expression to execute. The language can either be <tt>grel</tt>, <tt>jython</tt> or <tt>clojure</tt>,
    *                    e.g.: {@code grel:value.toLowercase()}
    * @param repeat      indicating whether or not this command should be repeated multiple times. A repeated command will be executed
    *                    until the result of the current iteration equals the result of the previous iteration.
    * @param repearCount the maximum amount of times a command will be repeated
    * @return the expression preview
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.3
    */
   ExpressionPreview expressionPreview(String projectId, long cellIndex, String expression, boolean repeat, int repearCount) throws IOException;
}
