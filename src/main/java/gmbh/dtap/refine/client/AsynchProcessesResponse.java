package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.ProcessStatus;

import java.util.Collections;
import java.util.List;

/**
 * This class represents the response from the <tt>check status of async processes</tt> request.
 *
 * @since 0.1.8
 */
class AsynchProcessesResponse {

   // TODO: https://github.com/dtap-gmbh/refine-java/issues/12

   public List<ProcessStatus> getProcessStatuses() {
      return Collections.emptyList();
   }
}
