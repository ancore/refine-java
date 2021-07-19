package com.ontotext.refine.client.command.processes;

import java.util.Collection;
import java.util.Objects;


/**
 * Holds the response from the {@link GetProcessesCommand}.
 *
 * @author Antoniy Kunchev
 */
public class GetProcessesCommandResponse {

  private final Collection<ProjectProcess> processes;

  public GetProcessesCommandResponse(Collection<ProjectProcess> processes) {
    this.processes = processes;
  }

  public Collection<ProjectProcess> getProcesses() {
    return processes;
  }

  /**
   * Represents a single process that could be returned from {@link GetProcessesCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class ProjectProcess {

    private Integer id;
    private int progress;
    private String description;
    private String status; // Enum maybe ?
    private boolean immediate;
    private Collection<Object> onDone;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public int getProgress() {
      return progress;
    }

    public void setProgress(int progress) {
      this.progress = progress;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public boolean isImmediate() {
      return immediate;
    }

    public void setImmediate(boolean immediate) {
      this.immediate = immediate;
    }

    public Collection<Object> getOnDone() {
      return onDone;
    }

    public void setOnDone(Collection<Object> onDone) {
      this.onDone = onDone;
    }

    @Override
    public String toString() {
      return new StringBuilder()
          .append("ProjectProcess [id=").append(id)
          .append(", progress=").append(progress)
          .append(", description=").append(description)
          .append(", status=").append(status)
          .append(", immediate=").append(immediate)
          .append(", onDone=").append(onDone)
          .append("]").toString();
    }

    @Override
    public int hashCode() {
      return Objects.hash(description, id, immediate, onDone, progress, status);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      ProjectProcess other = (ProjectProcess) obj;
      return Objects.equals(description, other.description)
          && Objects.equals(id, other.id)
          && immediate == other.immediate
          && Objects.equals(onDone, other.onDone)
          && progress == other.progress
          && Objects.equals(status, other.status);
    }
  }
}
